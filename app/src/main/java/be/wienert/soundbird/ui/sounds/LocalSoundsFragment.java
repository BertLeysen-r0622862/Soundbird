package be.wienert.soundbird.ui.sounds;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import be.wienert.soundbird.R;
import be.wienert.soundbird.data.model.Sound;
import be.wienert.soundbird.ui.addsound.AddSoundActivity;
import be.wienert.soundbird.ui.main.MainActivity;

public class LocalSoundsFragment extends SoundsFragment {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getLocalSounds().observe(this, sounds -> soundsRecyclerView.setSounds(sounds));

        registerForContextMenu(view);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteButton:
                delete(soundsRecyclerView.getLastClickedSound());
                break;
            case R.id.editButton:
                edit(soundsRecyclerView.getLastClickedSound());
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void edit(Sound sound) {
        /*Intent intent = new Intent(getActivity(), EditSoundActivity.class);

        Bundle extras = new Bundle();
        extras.putString("Url",sound.getUri().getPath());
        extras.putString("Uuid",sound.getUuid().toString());
        extras.putString("name",sound.getName());

        intent.putExtras(extras);
        startActivity(intent);*/

        dialogPopup(sound);
    }



    public void dialogPopup(Sound sound)
    {
        List<Sound> sounds = new ArrayList<>(Objects.requireNonNull(viewModel.getLocalSounds().getValue()));
        final Dialog d = new Dialog(getActivity()); // initialize dialog
        d.setContentView( R.layout.dialog_layout);
        d.setTitle("Edit Button");
        final EditText ed1= d.findViewById(R.id.soundNameEditText);
        ed1.setText(sound.getName());

        // initialize edittext using the dialog object.
        /*final TextView ed2= d.findViewById(R.id.selectedFileTextView);
        ed2.setText(sound.getUri().getPath());*/



        Button b = (Button) d.findViewById(R.id.confirmEditSoundButton);
        b.setOnClickListener(new View.OnClickListener() // button 1 click
        {

            @Override
            public void onClick(View v) {
                for (Sound s: sounds){
                    if (s.getUuid() == sound.getUuid()){
                        s.setName(ed1.getText().toString());
                    }
                }
                soundsRecyclerView.setSounds(sounds);
                d.dismiss();
            }


        });

        d.show(); // show the dialog
    }

    private void delete(Sound sound) {
        // Fake delete
        List<Sound> sounds = new ArrayList<>(Objects.requireNonNull(
                viewModel.getLocalSounds().getValue()));
        sounds.remove(sound);
        soundsRecyclerView.setSounds(sounds);

        Snackbar snackbar = Snackbar.make(Objects.requireNonNull(getView()), "Sound deleted", Snackbar.LENGTH_LONG);
        snackbar.setAction("Undo", v ->
                // Undo fake delete
                soundsRecyclerView.setSounds(viewModel.getLocalSounds().getValue()));

        snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                    viewModel.delete(sound);
                }

            }
        });
        snackbar.show();
    }
}
