package ua.tsopin.test;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.tsopin.test.databinding.FragmentLoginBinding;
import ua.tsopin.test.interfaces.iMain;
import ua.tsopin.test.utils.Screens;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private iMain mCallback;
    private FragmentLoginBinding binding;

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        binding.etEmail.setText(mCallback.getSettings().getLastLogin());
        binding.swtSaveLogin.setChecked(mCallback.getSettings().isSavelogin());
        binding.btnLogin.setOnClickListener(this);
        binding.btnSignup.setOnClickListener(this);

        return binding.getRoot();
    }

    private boolean validateForm(String login, String pass) {
        return (Patterns.EMAIL_ADDRESS.matcher(login).matches() && pass.length() >= 5);
    }

    private void login(String login, String pass, boolean saveLogin) {

        if (!validateForm(login, pass)) {
            mCallback.showMessage("Введіть логін і пароль");
            return;
        }
        mCallback.doLogin(login, pass);
        String lastLogin = login;

        mCallback.getSettings().setSavelogin(saveLogin);
        if (!saveLogin) {
            lastLogin = "";
        }
        mCallback.getSettings().setLastLogin(lastLogin);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                login(binding.etEmail.getText().toString(), binding.etPassword.getText().toString(), binding.swtSaveLogin.isChecked());
                break;
            case R.id.btn_signup:
                mCallback.switchScreen(Screens.SignUp);
                break;

        }
    }
}