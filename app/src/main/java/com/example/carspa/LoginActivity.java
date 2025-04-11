package com.example.carspa;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    private Button loginButton;
    private TextView signupText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);

        // Initialize UI elements
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        signupText = findViewById(R.id.signupText);

        // Prevent space characters in username and password
        InputFilter noSpaceFilter = (source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++) {
                if (Character.isWhitespace(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        };
        username.setFilters(new InputFilter[]{noSpaceFilter});
        password.setFilters(new InputFilter[]{noSpaceFilter});

        // Handle login button click
        loginButton.setOnClickListener(view -> {
            String user = username.getText().toString().trim();
            String pass = password.getText().toString().trim();

            // Validate credentials
            if (user.equals("admin") && pass.equals("1234")) {  // Replace with real authentication logic
                Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                // Navigate to MainActivity (which has the bottom menu)
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Close LoginActivity
            } else {
                Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle sign-up text click (Optional)
        signupText.setOnClickListener(view -> {
            Toast.makeText(LoginActivity.this, "Redirecting to Sign-up Page...", Toast.LENGTH_SHORT).show();
            // startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });
    }
}
