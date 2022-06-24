package ua.tsopin.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import ua.tsopin.test.databinding.ActivityMainBinding;
import ua.tsopin.test.interfaces.iMain;
import ua.tsopin.test.interfaces.onTaskDone;
import ua.tsopin.test.models.Note;
import ua.tsopin.test.utils.Screens;

public class MainActivity extends AppCompatActivity implements iMain {

    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fs_db;
    private LocalRepository lc_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        mAuth = FirebaseAuth.getInstance();
        fs_db = FirebaseFirestore.getInstance();
        lc_db = new LocalRepository(getApplicationContext());

        if (savedInstanceState == null) {
            switchScreen(Screens.FirstMain);
        }

        setContentView(binding.getRoot());
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            switchScreen(Screens.Login);
        }
    }

    @Override
    public void showMessage(String msg) {
        Snackbar.make(binding.mainContainer, msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showFilterDialog() {
        BottomDialogFragment bottomDialogFragment = new BottomDialogFragment();
        bottomDialogFragment.show(getSupportFragmentManager(), "filter_dialog");
    }

    @Override
    public void showProfileDialog() {
        ProfileFragment dialog = new ProfileFragment();
        dialog.show(getSupportFragmentManager(), "profile_dialog");
    }

    @Override
    public boolean doLogin(String login, String pass) {
        showProgress();
        mAuth.signInWithEmailAndPassword(login, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgress();
                        if (task.isSuccessful()) {
                            switchScreen(Screens.Main);
                        } else {
                            showMessage("Упс! Сталася якась халепа. Спробуйте ще раз.");
                        }
                    }
                });
        return true;
    }

    @Override
    public boolean doSignUp(String login, String pass) {
        showProgress();
        mAuth.createUserWithEmailAndPassword(login, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgress();
                        if (task.isSuccessful()) {
                            showMessage("Реєстрація успішна");
                            switchScreen(Screens.Main);
                        } else {
                            showMessage("Упс! Сталася якась халепа. Спробуйте ще раз."+task.getException().getLocalizedMessage());
                        }
                    }
                });
        return true;
    }

    @Override
    public void doLogout() {
        mAuth.signOut();
        switchScreen(Screens.Login);
    }

    @Override
    public void setCategory(int category) {

        if(category == -1) {
            loadNotes();
        } else {
            showProgress();
            lc_db.loadNotesInCategory(category, new onTaskDone<ArrayList<Note>>() {
                @Override
                public void onPostTask(ArrayList<Note> result) {
                    hideProgress();
                    getMainFragment().updateNoteList(result);
                }
            });
        }

    }

    @Override
    public void loadNotes() {
        showProgress();
        lc_db.loadNotes(new onTaskDone<ArrayList<Note>>() {
            @Override
            public void onPostTask(ArrayList<Note> result) {
                hideProgress();
                getMainFragment().updateNoteList(result);
            }
        });
    }


    @Override
    public void removeNote(int position, Note note) {

        lc_db.delete(note, new onTaskDone<Void>() {
            @Override
            public void onPostTask(Void result) {
                getMainFragment().removeNote(position);
            }
        });

    }

    @Override
    public void addNote(Note note) {
        FirebaseUser user = mAuth.getCurrentUser();

        lc_db.insert(note, new onTaskDone<Note>() {
            @Override
            public void onPostTask(Note result) {
                fs_db.collection(user.getUid())
                        .add(result)
                        .addOnFailureListener(e -> showMessage("Помилка при додавані нотатки."));
                goBack();
                getMainFragment().insertNote(result);
            }
        });
    }

    @Override
    public void editNote(int position, Note note) {
        switchScreen(Screens.Detail, note);
    }

    @Override
    public void updateNote(Note note) {
        FirebaseUser user = mAuth.getCurrentUser();

        lc_db.update(note, new onTaskDone<Note>() {
            @Override
            public void onPostTask(Note result) {
                goBack();
                getMainFragment().updateNote(result);
            }
        });

//        fs_db.collection(user.getUid())
//                .whereEqualTo("id", note.getId())
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                            }
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
    }

    public void showProgress() {
        binding.progressLayout.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        binding.progressLayout.setVisibility(View.GONE);
    }

    @Override
    public SettingsHandler getSettings() {
        return SettingsHandler.getInstance(MainActivity.this);
    }

    @Override
    public void goBack() {
        onBackPressed();
    }

    @Override
    public void switchScreen(Screens scr, Parcelable obj) {
        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();

        switch (scr) {
            case Login:
                tr.add(binding.mainContainer.getId(), LoginFragment.class, null);
                break;
            case SignUp:
                tr.addToBackStack(null).replace(binding.mainContainer.getId(), new SignupFragment());
                break;
            case FirstMain:
                tr.add(binding.mainContainer.getId(), MainFragment.class, null, "MAIN_FRAGMENT");
                break;
            case Main:
                tr.replace(binding.mainContainer.getId(), new MainFragment(), "MAIN_FRAGMENT");
                break;
            case Detail:
                tr.addToBackStack(null).replace(binding.mainContainer.getId(), EditFragment.newInstance(obj));
                break;
        }

        tr.commit();
    }

    @Override
    public void switchScreen(Screens scr) {
        switchScreen(scr, null);
    }

    private MainFragment getMainFragment() {
        return (MainFragment) getSupportFragmentManager().findFragmentByTag("MAIN_FRAGMENT");
    }

}