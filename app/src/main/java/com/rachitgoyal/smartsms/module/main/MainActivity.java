package com.rachitgoyal.smartsms.module.main;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.rachitgoyal.smartsms.R;
import com.rachitgoyal.smartsms.model.Model;
import com.rachitgoyal.smartsms.module.main.adapter.MessagesAdapter;
import com.rachitgoyal.smartsms.module.main.sms_listener.SmsReceiverService;
import com.rachitgoyal.smartsms.util.StickyHeaderItemDecoration;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.messages_rv)
    RecyclerView mMessagesRV;

    MessagesAdapter mMessagesAdapter;
    private ProgressDialog mProgressDialog;
    private SmsBroadcastReceiver mSmsBroadcastReceiver;

    public static Intent getActivityIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSmsBroadcastReceiver = new SmsBroadcastReceiver();
        registerReceiver(mSmsBroadcastReceiver, new IntentFilter(SmsBroadcastReceiver.ACTION));
        startService(SmsReceiverService.startService(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        new LoadMessagesAsyncTask(this).execute(getContentResolver());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSmsBroadcastReceiver != null) {
            unregisterReceiver(mSmsBroadcastReceiver);
        }
    }

    private void showMessages(List<Model> models) {
        mMessagesAdapter = new MessagesAdapter(models);
        mMessagesRV.setLayoutManager(new LinearLayoutManager(this));
        mMessagesRV.setAdapter(mMessagesAdapter);
        mMessagesRV.addItemDecoration(new StickyHeaderItemDecoration(mMessagesAdapter));
    }

    private static class LoadMessagesAsyncTask extends AsyncTask<ContentResolver, Void, List<Model>> {

        private WeakReference<MainActivity> activityReference;

        LoadMessagesAsyncTask(MainActivity activity) {
            this.activityReference = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            if (activity.mProgressDialog == null) {
                activity.mProgressDialog = new ProgressDialog(activity);
            }
            activity.mProgressDialog.setIndeterminate(true);
            activity.mProgressDialog.setMessage(activity.getString(R.string.loading_messages));
            activity.mProgressDialog.setCancelable(false);
            activity.mProgressDialog.show();
        }

        @Override
        protected List<Model> doInBackground(ContentResolver... contentResolvers) {
            MainPresenter presenter = new MainPresenter();
            return presenter.readAllSMS(contentResolvers[0]);
        }

        @Override
        protected void onPostExecute(List<Model> models) {
            super.onPostExecute(models);
            MainActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            if (activity.mProgressDialog != null && activity.mProgressDialog.isShowing()) {
                activity.mProgressDialog.dismiss();
                activity.mProgressDialog = null;
            }
            activity.showMessages(models);
        }
    }

    public class SmsBroadcastReceiver extends BroadcastReceiver {

        public static final String ACTION = "com.rachitgoyal.smartsms.SMS_RECEIVED";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals(ACTION)) {
                new LoadMessagesAsyncTask(MainActivity.this).execute(getContentResolver());
            }
        }
    }
}
