package ua.tsopin.test;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import ua.tsopin.test.interfaces.onTaskDone;
import ua.tsopin.test.models.Note;

public class LocalRepository {

    private DatabaseHandler databaseHandler;

    public LocalRepository(Context context) {
        databaseHandler = DatabaseHandler.getInstance(context);
    }

    public void insert(Note note, onTaskDone<Note> done) {
        new InsertAsync(databaseHandler, done).execute(note);
    }

    public void update(Note note, onTaskDone<Note> done) {
        new UpdateAsync(databaseHandler, done).execute(note);
    }

    public void delete(Note note, onTaskDone<Void> done) {
        new DeleteAsync(databaseHandler, done).execute(note);
    }

    public void loadNotes(onTaskDone<ArrayList<Note>> done) {
        new LoadAllAsync(databaseHandler, done).execute();
    }

    public void loadNotesInCategory(int category, onTaskDone<ArrayList<Note>> done) {
        new LoadInCategoryAsync(databaseHandler, done).execute(category);
    }

    private static class InsertAsync extends AsyncTask<Note, Void, Note>{
        private final DatabaseHandler db;
        private onTaskDone<Note> callback;

        public InsertAsync(DatabaseHandler db, onTaskDone<Note> done) {
            this.callback = done;
            this.db = db;
        }

        @Override
        protected Note doInBackground(Note... notes) {
            return db.addNote(notes[0]);
        }

        @Override
        protected void onPostExecute(Note note) {
            super.onPostExecute(note);

            if (callback != null) {
                callback.onPostTask(note);
            }

        }
    }

    private static class UpdateAsync extends AsyncTask<Note, Void, Note> {
        private final DatabaseHandler db;
        private onTaskDone<Note> callback;

        public UpdateAsync(DatabaseHandler db, onTaskDone<Note> done) {
            this.callback = done;
            this.db = db;
        }

        @Override
        protected Note doInBackground(Note... notes) {
            return db.updateNote(notes[0]);
        }

        @Override
        protected void onPostExecute(Note note) {
            super.onPostExecute(note);

            if (callback != null) {
                callback.onPostTask(note);
            }

        }
    }

    private static class DeleteAsync extends AsyncTask<Note, Void, Void> {
        private final DatabaseHandler db;
        private onTaskDone<Void> callback;

        public DeleteAsync(DatabaseHandler db, onTaskDone<Void> done) {
            this.callback = done;
            this.db = db;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            db.deleteNote(notes[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            if (callback != null) {
                callback.onPostTask(null);
            }

        }
    }

    private static class LoadAllAsync extends AsyncTask<Void, Void, ArrayList<Note>> {
        private final DatabaseHandler db;
        private onTaskDone<ArrayList<Note>> callback;

        public LoadAllAsync(DatabaseHandler db, onTaskDone<ArrayList<Note>> callback) {
            this.db = db;
            this.callback = callback;
        }

        @Override
        protected ArrayList<Note> doInBackground(Void... voids) {
            return db.getAllNotes();
        }

        @Override
        protected void onPostExecute(ArrayList<Note> notes) {
            super.onPostExecute(notes);

            if (callback != null) {
                callback.onPostTask(notes);
            }
        }
    }

    private static class LoadInCategoryAsync extends AsyncTask<Integer, Void, ArrayList<Note>> {
        private final DatabaseHandler db;
        private onTaskDone<ArrayList<Note>> callback;

        public LoadInCategoryAsync(DatabaseHandler db, onTaskDone<ArrayList<Note>> callback) {
            this.db = db;
            this.callback = callback;
        }

        @Override
        protected ArrayList<Note> doInBackground(Integer... ints) {
            return db.getNotesInCategory(ints[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<Note> notes) {
            super.onPostExecute(notes);

            if (callback != null) {
                callback.onPostTask(notes);
            }
        }
    }
}
