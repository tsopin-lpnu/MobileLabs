package ua.tsopin.test;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import ua.tsopin.test.databinding.FragmentSignupBinding;
import ua.tsopin.test.interfaces.iMain;
import ua.tsopin.test.utils.Screens;

public class SignupFragment extends Fragment {

    private iMain mCallback;
    private FragmentSignupBinding binding;

    public SignupFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignupBinding.inflate(inflater, container, false);
        binding.btnSignupReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup(binding.etLoginReg.getText().toString(), binding.etPassReg.getText().toString(), binding.etPassRetryReg.getText().toString());
            }
        });
        return binding.getRoot();
    }

    private boolean validateForm(String login, String pass, String retry_pass) {
        return (Patterns.EMAIL_ADDRESS.matcher(login).matches() && pass.length() >= 5 && pass.equals(retry_pass));
    }

    private void signup(String login, String pass, String retry_pass) {

        if (!validateForm(login, pass, retry_pass)) {
            mCallback.showMessage("Введіть логін і пароль");
            return;
        }

        mCallback.doSignUp(login, pass);
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            mCallback = (iMain) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement iMain");
        }
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }
}