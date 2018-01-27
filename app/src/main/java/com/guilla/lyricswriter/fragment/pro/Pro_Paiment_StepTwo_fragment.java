package com.guilla.lyricswriter.fragment.pro;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.guilla.lyricswriter.BO.UserLocation;
import com.guilla.lyricswriter.R;
import com.guilla.lyricswriter.Utils.Dialog_TPE_Businness;
import com.guilla.lyricswriter.Utils.ToastInterface;
import com.guilla.lyricswriter.Utils.Utils;
import com.guilla.lyricswriter.fragment.Updateable;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.util.Log.d;


/**
 * Created by Moi on 14/11/15.
 */
public class Pro_Paiment_StepTwo_fragment extends Fragment implements Updateable,ToastInterface, View.OnClickListener {
    private View inflate;
    GridView grid;
    private TextView valider;
    private EditText amount_paiment;
    private EditText tpe;
    private TextView sharepoint_supplementary;
    private CircleImageView picture_profil;
    private TextView username_login;
    private ImageView cash;
    private ImageView CB;
    private UserLocation userLocation;
    private ToastInterface toastInterface;
    private Dialog_TPE_Businness.TPEDialog tpeDialog;
    private Dialog_TPE_Businness dialog;
    private ImageView modifyTPE;

    public static Pro_Paiment_StepTwo_fragment newInstance(UserLocation userLocation) {
        Pro_Paiment_StepTwo_fragment myFragment = new Pro_Paiment_StepTwo_fragment();
        Bundle args = new Bundle();
        args.putSerializable("user", userLocation);
        myFragment.setArguments(args);
        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        inflate = inflater.inflate(R.layout.fragment_paiment_steptwo_normal_pro, container, false);
        toastInterface = this;
        valider = (TextView) inflate.findViewById(R.id.valider);
        amount_paiment = (EditText) inflate.findViewById(R.id.amount_paiment);
        tpe = (EditText) inflate.findViewById(R.id.tpe);
        sharepoint_supplementary = (TextView) inflate.findViewById(R.id.sharepoint_supplementary);
        picture_profil = (CircleImageView) inflate.findViewById(R.id.picture_profil);
        username_login = (TextView) inflate.findViewById(R.id.username_login);
        modifyTPE = (ImageView) inflate.findViewById(R.id.modifyTPE);
        modifyTPE.setVisibility(View.INVISIBLE);


        // amount_paiment.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
        cash = (ImageView) inflate.findViewById(R.id.cash);
        CB = (ImageView) inflate.findViewById(R.id.CB);
        cash.setOnClickListener(this);
        CB.setOnClickListener(this);
        valider.setOnClickListener(this);

        userLocation = (UserLocation) getArguments().getSerializable("user");
        username_login.setText(userLocation.getUsername());

        if (userLocation.getPictureProfil() != null) {
            Bitmap bmp = null;
            bmp = BitmapFactory.decodeByteArray(userLocation.getPictureProfil(), 0, userLocation.getPictureProfil().length);
            picture_profil.setImageBitmap(bmp);
        }

       // getTPE_preferences();


        tpe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowTPEDialog();
            }
        });

        modifyTPE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowTPEDialog();
            }
        });

        return inflate;
    }


    @Override
    public void update() {
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.valider:
                if (Utils.isConnected(getActivity())) {
                    //  SendNotification sendNotification= new SendNotification(getActivity(),getActivity(),userLocation,toastInterface);
                    ///  sendNotification.Send(amount_paiment.getText().toString());
                } else {
                    Toast.makeText(getActivity(), "Veuillez vous connécter à un réseau", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.cash:
                cash.setImageResource(R.drawable.cash_icone_on);
                CB.setImageResource(R.drawable.cb_icone);
                break;
            case R.id.CB:
                cash.setImageResource(R.drawable.cash_icone);
                CB.setImageResource(R.drawable.cb_icone_on);

                break;

        }
    }

    @Override
    public void onNotificationError() {

    }

    @Override
    public void onNotificationSuccess(UserLocation location, String price) {
        Toast.makeText(getActivity(), "Notification envoyée", Toast.LENGTH_LONG).show();
        CreateTransaction(location, price);
    }


    private void CreateTransaction(final UserLocation user, final String price) {}

    private void CreateCISSTransaction(final String transactionId, final String price, final UserLocation client) {}


    //Get All transaction for self business to count Generated_sharepoints
    private void UpdateBusiness() {}


    //Update Business value : sharepoint_generated
    public void UpdateBusiness_sharepoint(String objectId, final int sharepoints) {}

    private void DoPaiment() {

    }


   /* Call post(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .addHeader("Content-Type","application/json")
                .addHeader("X-Parse-Application-Id","process.env.PARSE_APP_ID")
                .addHeader("X-Parse-Master-Key","process.env.PARSE_MASTER_KEY")
                .addHeader("X-Parse-Session-Token",parseUser.getSessionToken())
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }*/

   /* private void SendNotification(UserLocation user, final EditText price){
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject param = new JSONObject();
            JSONObject data = new JSONObject();
            param.put("title", "Notification");
            param.put("body", LocationUserActivity.db.getBusinessName()+ " vous à envoyé une notification ");
            data.put("data", param);
            jsonObject.put("payload", data);
            Log.d("jsonout",jsonObject.toString());
            post("http://ec2-52-56-157-252.eu-west-2.compute.amazonaws.com/api/paiment/"+user.getId(), jsonObject.toString(), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("Response Push Failed", e.getMessage());
                            toastInterface.onNotificationError();

                            //Something went wrong
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                String responseStr = response.body().string();
                                Log.d("Response", responseStr);

                                if (Pro_Location!=null){
                                    Pro_Location.runOnUiThread(new Runnable() {
                                        public void run() {
                                            toastInterface.onNotificationSuccess(p,price.getText().toString());
                                        }
                                    });
                                }
                            } else {
                                // toastInterface.onNotificationError();
                                if (Pro_Location!=null){
                                    Pro_Location.runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(getContext(), "Une erreur s'est produite lors de la notification", Toast.LENGTH_LONG).show();
                                            //  toastInterface.onNotificationSuccess();
                                        }
                                    });
                                }
                                Log.d("Response Push Failed", response.message());
                            }
                        }
                    }
            );
        } catch (JSONException ex) {
            Log.d("Exception", "JSON exception", ex);
        }
    }*/





    private void ShowTPEDialog(){}
}