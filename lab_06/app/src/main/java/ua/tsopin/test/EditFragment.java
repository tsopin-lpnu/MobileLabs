package ua.tsopin.test;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import java.util.Date;

import ua.tsopin.test.databinding.FragmentEditBinding;
import ua.tsopin.test.interfaces.iMain;
import ua.tsopin.test.models.Note;
import ua.tsopin.test.utils.Utils;

public class EditFragment extends Fragment {

    private static final String ARG_NOTE_OBJ = "note_obj";

    private Note note = null;
    private iMain mCallback;
    private FragmentEditBinding binding;

    public EditFragment() {
    }

    public static EditFragment newInstance(Parcelable obj) {
        EditFragment fragment = new EditFragment();
        if (obj != null) {
            Bundle args = new Bundle();
            args.putParcelable(ARG_NOTE_OBJ, obj);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note = getArguments().getParcelable(ARG_NOTE_OBJ);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditBinding.inflate(inflater, container, false);

        if (note != null) {
            binding.etTitle.setText(note.getTitle());
            binding.etDesc.setText(note.getDesc());

            int category = note.getCategory();

            if (category >= 0 && binding.rgColorNote.getChildCount() > category) {
                ((RadioButton) binding.rgColorNote.getChildAt(category)).setChecked(true);
            }
        }

        binding.bottomAppBar.setNavigationOnClickListener(v -> mCallback.goBack());

        binding.fab.setOnClickListener(v -> {
            String title = binding.etTitle.getText().toString().trim();
            String desc = binding.etDesc.getText().toString().trim();
            int category = binding.rgColorNote.indexOfChild(binding.rgColorNote.findViewById(binding.rgColorNote.getCheckedRadioButtonId()));

            if(desc.isEmpty()) {
                mCallback.showMessage("Введіть нотатку");
                return;
            }
            if (title.isEmpty()) {
                title = Utils.getTitleFromDesc(desc);
            }

            if (note == null) {
                mCallback.addNote(new Note(title, desc, category));
            } else {
                note.setTitle(title);
                note.setDesc(desc);
                note.setCategory(category);
                note.setUpdated_at(new Date());

                mCallback.updateNote(note);
            }
        });

        return binding.getRoot();
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