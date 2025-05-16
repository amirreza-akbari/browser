package com.example.healthcheckapp;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private EditText urlInput;
    private ImageButton backButton, forwardButton, goButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // فرض کنید نام فایل layout شما activity_main.xml است

        // Initialize views
        webView = findViewById(R.id.webView);
        urlInput = findViewById(R.id.urlInput);
        backButton = findViewById(R.id.backButton);
        forwardButton = findViewById(R.id.forwardButton);
        goButton = findViewById(R.id.goButton);

        // Configure WebView
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        // Load default page (optional)
        webView.loadUrl("file:///android_asset/index.html");

        // Set click listeners
        backButton.setOnClickListener(v -> {
            if (webView.canGoBack()) {
                webView.goBack();
            }
        });

        forwardButton.setOnClickListener(v -> {
            if (webView.canGoForward()) {
                webView.goForward();
            }
        });

        goButton.setOnClickListener(v -> {
            String inputText = urlInput.getText().toString().trim();
            if (!inputText.isEmpty()) {
                processUserInput(inputText);
            }
        });

        // Handle keyboard "Enter" key
        urlInput.setOnEditorActionListener((v, actionId, event) -> {
            String inputText = urlInput.getText().toString().trim();
            if (!inputText.isEmpty()) {
                processUserInput(inputText);
            }
            return true;
        });
    }

    private void processUserInput(String input) {
        // Check if input is a URL
        if (isValidUrl(input)) {
            loadUrl(fixUrl(input));
        } else {
            // Treat as search query
            searchWeb(input);
        }
    }

    private boolean isValidUrl(String input) {
        // Simple URL validation (improve as needed)
        return input.matches("^(https?://)?([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?$") ||
                input.matches("^[\\w-]+\\.[\\w-]+(/[\\w- ./?%&=]*)?$");
    }

    private String fixUrl(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return "https://" + url;
        }
        return url;
    }

    private void searchWeb(String query) {
        try {
            // Encode the query for URL (supports Persian and other languages)
            String encodedQuery = URLEncoder.encode(query, "UTF-8");
            String searchUrl = "https://www.google.com/search?q=" + encodedQuery;
            loadUrl(searchUrl);
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(this, "Error encoding search query", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadUrl(String url) {
        webView.loadUrl(url);
        urlInput.setText(url); // Show the loaded URL in the input field
    }

    // Handle back button press for WebView navigation
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}