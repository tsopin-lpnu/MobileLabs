package ua.tsopin.test;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import ua.tsopin.test.databinding.NoteItemBinding;
import ua.tsopin.test.models.Note;
import ua.tsopin.test.utils.Utils;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    //private Context mContext;
    private NoteAdapter.onNoteListener itemClickListener;
    private ArrayList<Note> data = new ArrayList<>();

    public NoteAdapter(NoteAdapter.onNoteListener listener) {
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        vh.root_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onNoteClick(vh.getAdapterPosition(), data.get(vh.getAdapterPosition()));
                }
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = data.get(position);

        holder.tv_title.setText(note.getTitle());
        holder.tv_desc.setText(note.getDesc());
        holder.tv_date.setText(Utils.formateDate(note.getUpdated_at()));
        holder.root_layout.setBackgroundColor(note.getCategory_color());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public Note getNote(int position) {
        return data.get(position);
    }

    public void insertNote(Note note) {
        data.add(0, note);
        notifyItemInserted(0);
    }

    public void removeNote(int index) {
        data.remove(index);
        notifyItemRemoved(index);
    }

    public void updateNote(Note note) {
        data.set(0, note);
        notifyItemChanged(0);
    }

    public void updateNoteList(ArrayList<Note> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void setListener(NoteAdapter.onNoteListener listener) {
        itemClickListener = listener;
    }

    public void onDetach() {
        //this.mContext = null;
        this.itemClickListener = null;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_desc;
        TextView tv_date;
        View root_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            tv_date = itemView.findViewById(R.id.tv_datetime);
            root_layout = itemView.findViewById(R.id.item_root);
        }
    }

    public interface onNoteListener {
        void onNoteClick(int position, Note note);
    }
}
