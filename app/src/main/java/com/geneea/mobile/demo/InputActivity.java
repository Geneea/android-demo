package com.geneea.mobile.demo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class InputActivity extends AppCompatActivity {

    /**
     * Keep track of the analysis task to ensure we can cancel it if requested.
     */
    private AnalysisTask mAnalysisTask = null;

    // UI references
    private EditText mTextToAnalyze;
    private View mProgressView;
    private View mAnalysisFormView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        setupActionBar();

        mTextToAnalyze = (EditText) findViewById(R.id.analysis_input);
        mTextToAnalyze.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(final TextView textView, final int id, final KeyEvent keyEvent) {
                if (id == R.id.analysis_input || id == EditorInfo.IME_NULL) {
                    runAnalysis();
                    return true;
                }
                return false;
            }
        });

        final Button mAnalyzeButton = (Button) findViewById(R.id.analyze_button);
        mAnalyzeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runAnalysis();
            }
        });

        mAnalysisFormView = findViewById(R.id.analysis_form);
        mProgressView = findViewById(R.id.analysis_progress);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_input, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Shows the progress UI and hides the form.
     */
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            final int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mAnalysisFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mAnalysisFormView.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mAnalysisFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mAnalysisFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Calls the analysis API.
     */
    private void runAnalysis() {
        if (mAnalysisTask != null) {
            return;
        }

        mTextToAnalyze.setError(null);
        final String text = mTextToAnalyze.getText().toString();

        if (TextUtils.isEmpty(text)) {
            mTextToAnalyze.setError(getString(R.string.error_text_required));
            mAnalysisFormView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAnalysisTask = new AnalysisTask();
            mAnalysisTask.execute(text);
        }
    }

    /**
     * Represents an asynchronous analysis task.
     */
    private class AnalysisTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(final String... params) {
            // TODO: call API here

            try {
                Thread.sleep(4_000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAnalysisTask = null;
            showProgress(false);
            finish();
        }

        @Override
        protected void onCancelled() {
            mAnalysisTask = null;
            showProgress(false);
        }
    }
}
