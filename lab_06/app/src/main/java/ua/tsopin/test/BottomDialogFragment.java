package ua.tsopin.test;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

import ua.tsopin.test.databinding.FragmentBottomsheetBinding;
import ua.tsopin.test.interfaces.iMain;

public class BottomDialogFragment extends BottomSheetDialogFragment {

    FragmentBottomsheetBinding binding;
    private iMain mCallback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBottomsheetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.navigationView.setItemIconTintList(null);
        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int category = -1;

                switch (item.getItemId()){
                    case R.id.filter_cat_1:
                        category = 0;
                        break;
                    case R.id.filter_cat_2:
                        category = 1;
                        break;
                    case R.id.filter_cat_3:
                        category = 2;
                        break;
                    case R.id.filter_cat_4:
                        category = 3;
                        break;
                    case R.id.filter_cat_5:
                        category = 4;
                        break;
                    case R.id.filter_cat_6:
                        category = 5;
                        break;
                }

                mCallback.setCategory(category);
                dismiss();

                return true;
            }
        });
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
