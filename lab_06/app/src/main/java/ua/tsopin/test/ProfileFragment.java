package ua.tsopin.test;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ua.tsopin.test.databinding.FragmentProfileBinding;
import ua.tsopin.test.interfaces.iMain;

public class ProfileFragment extends AppCompatDialogFragment {

    private FragmentProfileBinding binding;
    private iMain mCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_profile, null);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        builder.setPositiveButton("", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setNegativeButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        TextView tv_email = v.findViewById(R.id.tv_profile_email);
        if(currentUser != null && currentUser.getEmail() != null){
            tv_email.setText(currentUser.getEmail());
        }
        Button btn_logout = v.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.doLogout();
                dismiss();
            }
        });

        builder.setView(v);
        return builder.create();
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
