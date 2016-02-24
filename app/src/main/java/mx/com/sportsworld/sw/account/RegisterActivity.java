package mx.com.sportsworld.sw.account;

import android.net.http.SslError;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.fragment.dialog.ProgressDialogFragment;

public class RegisterActivity extends SherlockFragmentActivity
{
    /** The Constant FRAG_TAG_LOADING. */
    private static final String FRAG_TAG_LOADING = "frag_tag_loading";

    private static final String URL_REGISTER = "https://atletas.upster.com.mx/usuario/registroApp";

    private WebView webview;

    /** The progress. */
    public static ProgressDialogFragment progress;
    /** The dialgo fragment. */
    Fragment dialgoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //getSupportActionBar().setTitle("Registro");
        setContentView(R.layout.web_view);

        webview = (WebView) findViewById(R.id.webView1);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (progress != null)
                    progress.dismiss();
            }
        });

        showFragmentsDialog(false);

        webview.loadUrl(URL_REGISTER);
    }

    /**
     * Show fragments dialog.
     *
     * @param savedInstanceState the saved instance state
     */
    public void showFragmentsDialog(boolean savedInstanceState) {
        dialgoFragment = getSupportFragmentManager().findFragmentByTag(
                ProgressDialogFragment.progressDialogTag);

        if (savedInstanceState == true) {
            dialgoFragment = getSupportFragmentManager().findFragmentByTag(
                    ProgressDialogFragment.progressDialogTag);
            progress = (ProgressDialogFragment) dialgoFragment;

        } else {

            if (dialgoFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .remove(dialgoFragment).commit();
            }

            progress = ProgressDialogFragment
                    .newInstance(RegisterActivity.this);
            progress.show(getSupportFragmentManager(),
                    ProgressDialogFragment.progressDialogTag);
        }

    }
}
