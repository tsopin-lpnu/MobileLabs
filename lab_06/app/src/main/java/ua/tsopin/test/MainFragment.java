package ua.tsopin.test;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ua.tsopin.test.databinding.FragmentMainBinding;
import ua.tsopin.test.interfaces.iMain;
import ua.tsopin.test.models.Note;
import ua.tsopin.test.utils.Screens;


public class MainFragment extends Fragment implements NoteAdapter.onNoteListener {

    private iMain mCallback;
    private FragmentMainBinding binding;
    private NoteAdapter noteAdapter;

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);

        binding.rvMain.setHasFixedSize(true);
        binding.rvMain.setLayoutManager(new LinearLayoutManager(getActivity()));

        noteAdapter = new NoteAdapter(this);
        binding.rvMain.setAdapter(noteAdapter);
        mCallback.loadNotes();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAbsoluteAdapterPosition();
                mCallback.removeNote(position, noteAdapter.getNote(position));
            }
        }).attachToRecyclerView(binding.rvMain);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.switchScreen(Screens.Detail);
            }
        });

        binding.bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mi_category:
                        mCallback.showFilterDialog();
                        break;
                    case R.id.mi_profile:
                        mCallback.showProfileDialog();
                        break;
                }
                return true;
            }
        });

        return binding.getRoot();
    }

    public void insertNote(Note note) {
        noteAdapter.insertNote(note);
    }

    public void removeNote(int index) {
        noteAdapter.removeNote(index);
    }

    public void updateNote(Note note) {
        noteAdapter.updateNote(note);
    }

    public void updateNoteList(ArrayList<Note> data) {
        noteAdapter.updateNoteList(data);
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
    //taras.sopin.itisz.2019@lpnu.ua
    @Override
    public void onDetach() {
        noteAdapter.onDetach();
        mCallback = null;
        super.onDetach();
    }

    @Override
    public void onNoteClick(int position, Note note) {
        mCallback.editNote(position, note);
    }
}