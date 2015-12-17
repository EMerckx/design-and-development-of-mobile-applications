package be.ugent.oomt.labo4;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import be.ugent.oomt.labo4.contentprovider.MessageProvider;
import be.ugent.oomt.labo4.contentprovider.database.DatabaseContract;

/**
 * Created by elias on 12/01/15.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private TextView summary;
    private TextView title;
    private EditText editText;
    private static final String CONTACT_KEY = "contact";

    public String getShownContact() {
        return getArguments().getString(CONTACT_KEY, "");
    }

    public static DetailFragment newInstance(String contact) {
        DetailFragment f = new DetailFragment();

        Bundle args = new Bundle();
        args.putString(CONTACT_KEY, contact);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        title = (TextView) view.findViewById(R.id.detail_title);
        summary = (TextView) view.findViewById(R.id.detail_summary);
        View footer = view.findViewById(R.id.footer);
        editText = (EditText) footer.findViewById(R.id.send_text);
        Button btnSend = (Button) footer.findViewById(R.id.btn_send);
        footer.setVisibility((getShownContact().equals(MqttHandler.clientId)) ? View.VISIBLE : View.GONE);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: send message to mqtt topic users/<client-id> where the <client-id> is the user who is sending this message.
                MqttHandler handler = MqttHandler.getInstance();
                MqttMessage message = new MqttMessage();
                message.setPayload(editText.getText().toString().getBytes());

                try {
                    handler.getClient().subscribe("/users/"+MqttHandler.clientId,2);
                    handler.getClient().publish("/users/"+MqttHandler.clientId,editText.getText().toString().getBytes(),2, true);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
        final Button btnUnsubscribe = (Button) view.findViewById(R.id.btn_unsubscribe);
        btnUnsubscribe.setVisibility((getShownContact().equals(MqttHandler.clientId)) ? View.GONE : View.VISIBLE);
        btnUnsubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnUnsubscribe.setVisibility(View.GONE);
                // TODO: unsubscribe from the topic users/<shown-contact> where the <shown-contact> is the user of the currently shown feed.
            }
        });
        title.setText(getShownContact());
        summary.setText("");

        getLoaderManager().initLoader(0, null, this);

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                DatabaseContract.Message._ID,
                DatabaseContract.Message.COLUMN_NAME_MESSAGE
        };
        String orderBy = DatabaseContract.Message.COLUMN_NAME_DATE + " ASC";
        return new CursorLoader(getActivity(), MessageProvider.MESSAGES_CONTENT_URL, projection, DatabaseContract.Message.COLUMN_NAME_CONTACT + " =?", new String[]{getShownContact()}, orderBy);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        summary.setText("");
        while (data.moveToNext()) {
            summary.append(data.getString(data.getColumnIndex(DatabaseContract.Message.COLUMN_NAME_MESSAGE)) + "\n");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}
}
