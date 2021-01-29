package com.example.admin.appcom;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link form_frag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link form_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class form_frag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public form_frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment form_frag.
     */
    // TODO: Rename and change types and number of parameters
    public static form_frag newInstance(String param1, String param2) {
        form_frag fragment = new form_frag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private EditText uname,uemail;
    private EditText uphone;
    private Button button,imgButton;
    private ImageView imageView;
    private static final int Gallery_request = 100;
    private Uri image_uri;
    private boolean flag1=false,flag2=false,flag3=false,isFlag4=false;
    private   Bitmap bitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }
    void update(){


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_form_frag, container, false);
        uname = view.findViewById(R.id.uname);
        uemail = view.findViewById(R.id.uemail);
        uphone = view.findViewById(R.id.umob_number);
        imageView = view.findViewById(R.id.imageView);
        button = view.findViewById(R.id.button);
        imgButton = view.findViewById(R.id.image_btn);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, Gallery_request);

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name  = uname.getText().toString();
                String email = uemail.getText().toString();
              String phone  = uphone.getText().toString();


                if (TextUtils.isEmpty(name)) {
                    uname.setError("Invalid Name");
                flag1= false;
                }
                else  flag1 = true;
                if ((TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
                flag2 =false;
                    uemail.setError("Invalid Email");
                }else  flag2 = true;
                    if (TextUtils.isEmpty(phone) || !Patterns.PHONE.matcher(phone).matches() ) {
                        uphone.setError("Invalid Phone number");
                flag3=false;
                }else {
                        flag3 = true;


                    }
            if (!isFlag4)
                Toast.makeText(getActivity(), "Please select Image", Toast.LENGTH_SHORT).show();
            if (flag1 && flag2 && flag3 && isFlag4)
            {  uname.setText("");
            uphone.setText("");
            uemail.setText("");
                imageView.setVisibility(View.GONE);
                isFlag4 = false;
                image_uri=null;
                ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Creating Contact...");
                progressDialog.show();
                SQLiteHelper sqLiteHelper = new SQLiteHelper(getActivity(),new sharedPrefData().getUserData(getActivity()).getEmail());
                byte[] data =getBitmapAsByteArray(bitmap);
                boolean flag = sqLiteHelper.insertContact(name,phone,email,data);
                if (flag)
                Toast.makeText(getActivity(), "Contact Saved", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }

            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallery_request && resultCode == RESULT_OK) {

            image_uri = data.getData();
            File f = new File(image_uri+"");
            imgButton.setText(f.getName());
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageURI(image_uri);
            isFlag4 = true;

            try {
              bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), image_uri);

            } catch (IOException e) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                isFlag4 = false;
                image_uri=null;
            }


        }else{
            isFlag4 = false;
            image_uri=null;
            imageView.setVisibility(View.GONE);
            imgButton.setText("Upload Image");
            Toast.makeText(getActivity(), "No Image Selected", Toast.LENGTH_SHORT).show();
        }


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
}
