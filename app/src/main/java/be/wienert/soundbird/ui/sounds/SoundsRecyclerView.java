package be.wienert.soundbird.ui.sounds;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import be.wienert.soundbird.R;
import be.wienert.soundbird.data.model.Sound;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SoundsRecyclerView extends RecyclerView.Adapter<SoundsRecyclerView.SoundViewHolder> {

    private List<Sound> sounds = new ArrayList<>();
    private SoundClickListener onClickListener = null;
    private SoundClickListener onLongClickListener = null;

    public void setSounds(List<Sound> sounds) {
        this.sounds = sounds;
        notifyDataSetChanged();
    }

    public void setOnClickListener(SoundClickListener listener) {
        onClickListener = listener;
    }

    public void setOnLongClickListener(SoundClickListener listener) {
        onLongClickListener = listener;
    }

    @NonNull
    @Override
    public SoundsRecyclerView.SoundViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SoundsRecyclerView.SoundViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.sound_layout, viewGroup, false), this);
    }

    @Override
    public void onBindViewHolder(@NonNull SoundsRecyclerView.SoundViewHolder boardsViewHolder, int i) {
        Sound sound = sounds.get(i);
        boardsViewHolder.name.setText(sound.getName());
    }

    @Override
    public int getItemCount() {
        return sounds == null ? 0 : sounds.size();
    }

    public interface SoundClickListener {
        void onClick(Sound sound);
    }

    static class SoundViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.nameTextView)
        Button name;

        SoundViewHolder(View view, SoundsRecyclerView soundsRecyclerView) {
            super(view);
            ButterKnife.bind(this, view);
            name.setOnClickListener(v -> {
                if (soundsRecyclerView.onClickListener != null) {
                    Sound sound = soundsRecyclerView.sounds.get(this.getAdapterPosition());
                    soundsRecyclerView.onClickListener.onClick(sound);
                }
            });
            name.setOnLongClickListener(v -> {
                if (soundsRecyclerView.onLongClickListener != null) {
                    Sound sound = soundsRecyclerView.sounds.get(this.getAdapterPosition());
                    soundsRecyclerView.onLongClickListener.onClick(sound);
                    return true;
                }
                return false;
            });
        }
    }
}

