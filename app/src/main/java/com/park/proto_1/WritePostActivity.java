package com.park.proto_1;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

public class WritePostActivity extends BasicActivity{
    private static final String TAG = "WritePostActivity";
    private FirebaseUser user;
    private ArrayList<String> pathList = new ArrayList<>();
    private LinearLayout parent;
    private RelativeLayout buttonsBackgroundLayout;
    private ImageView selectedImageView;
    private EditText selectedEditText;

    private int pathCount;
    private int  okCount;
    private TextView pointText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);


        buttonsBackgroundLayout = findViewById(R.id.buttonsBackgroundLayout);
        parent = findViewById(R.id.contentsLayout);

        buttonsBackgroundLayout.setOnClickListener(onClickListener);
        findViewById(R.id.check).setOnClickListener(onClickListener);
        findViewById(R.id.shot).setOnClickListener(onClickListener);
        findViewById(R.id.imageModify).setOnClickListener(onClickListener);
        findViewById(R.id.delete).setOnClickListener(onClickListener);
        findViewById(R.id.contentsEditText).setOnFocusChangeListener(onFocusChangeListener);
        findViewById(R.id.titleEditText).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    selectedEditText = null;
                }
            }
        });

        pointText = findViewById(R.id.point);
        PlusPoint();
        NowPoint();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0 :
                if(resultCode == Activity.RESULT_OK) {
                    String profilePath = data.getStringExtra("profilePath");
                    pathList.add(profilePath);

                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    LinearLayout linearLayout = new LinearLayout(WritePostActivity.this);
                    linearLayout.setLayoutParams(layoutParams);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);

                    if(selectedEditText == null){
                        parent.addView(linearLayout);
                    }else{
                        for(int i=0; i<parent.getChildCount(); i++){
                            if(parent.getChildAt(i)==selectedEditText.getParent()){
                                parent.addView(linearLayout, i+1);
                                break;
                            }
                        }
                    }

                    ImageView imageView = new ImageView(WritePostActivity.this);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            buttonsBackgroundLayout.setVisibility(View.VISIBLE);
                            selectedImageView = (ImageView) view;
                        }
                    });
                    Glide.with(this).load(profilePath).centerCrop().override(1000).into(imageView);
                    parent.addView(imageView);

                    EditText editText = new EditText(WritePostActivity.this);
                    editText.setLayoutParams(layoutParams);
                    editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
                    editText.setHint("내용");
                    editText.setOnFocusChangeListener(onFocusChangeListener);
                    linearLayout.addView(editText);
                }
                break;
            case 1:
                if(resultCode == Activity.RESULT_OK) {
                    String profilePath = data.getStringExtra("profilePath");
                    Glide.with(this).load(profilePath).centerCrop().override(1000).into(selectedImageView);
                }
                break;
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.check:
                    contentsUpdate();
                    break;
                case R.id.shot:
                    mystartActivity(GalleryActivity.class, 0);
                    break;
                case R.id.buttonsBackgroundLayout:
                    if(buttonsBackgroundLayout.getVisibility()==View.VISIBLE){
                        buttonsBackgroundLayout.setVisibility(View.GONE);
                    }
                    break;
                case R.id.imageModify:
                    mystartActivity(GalleryActivity.class, 1);
                    buttonsBackgroundLayout.setVisibility(View.GONE);
                    break;
                case R.id.delete:
                    parent.removeView((View) selectedImageView.getParent());
                    buttonsBackgroundLayout.setVisibility(View.GONE);
                    break;
            }
        }
    };

    View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if(b){
                selectedEditText = (EditText) view;
            }
        }
    };

    private void contentsUpdate(){
        final String title = ((EditText) findViewById(R.id.titleEditText)).getText().toString();
        if(title.length()>0){
            final ArrayList<String> contentsList = new ArrayList<>();
            user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            final DocumentReference documentReference = firebaseFirestore.collection("posts").document();

            for(int i=0; i<parent.getChildCount(); i++){
                LinearLayout linearLayout = (LinearLayout) parent.getChildAt(i);
                for(int j=0; j<parent.getChildCount(); j++){
                    View view = linearLayout.getChildAt(j);
                    if(view instanceof EditText){
                        String text = ((EditText)view).getText().toString();
                        if(text.length() > 0){
                            contentsList.add(text);
                        }
                    }else{
                        contentsList.add(pathList.get(pathCount));
                        String[] pathArray = pathList.get(pathCount).split("\\.");
                        final StorageReference mountainImagesRef = storageRef.child("posts/"+ documentReference.getId() +"/"+pathCount+"."+pathArray[pathArray.length-1]);
                        try {
                            InputStream stream = new FileInputStream(new File(pathList.get(pathCount)));

                            StorageMetadata metadata = new StorageMetadata.Builder()
                                    .setCustomMetadata("index", ""+(contentsList.size()-1))
                                    .build();

                            UploadTask uploadTask = mountainImagesRef.putStream(stream, metadata);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    final int index =Integer.parseInt(taskSnapshot.getMetadata().getCustomMetadata("index"));

                                    mountainImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Log.e("로그", "uri"+uri);
                                            contentsList.set(index, uri.toString());
                                            okCount++;
                                            if(pathList.size() == okCount){
                                                //완료
                                                PostInfo postInfo = new PostInfo(title, contentsList, user.getUid(), new Date());
                                                PlusPoint();
                                                startToast("게시물이 등록되었습니다. 500point 충전되었습니다.");
                                                dbuploader(documentReference, postInfo);
                                                for(int a=0; a<contentsList.size(); a++){
                                                    Log.e("로그", "콘텐츠: "+contentsList.get(a));
                                                }
                                            }
                                        }
                                    });
                                }
                            });
                        } catch (FileNotFoundException e){
                            Log.e("로그", "에러 "+e.toString());
                        }
                        pathCount++;
                    }
                }
            }
            if(pathList.size()==0){
                PostInfo postInfo = new PostInfo(title, contentsList, user.getUid(), new Date());
                dbuploader(documentReference, postInfo);
            }
        } else{
            startToast("제목, 내용을 입력해주세요.");
        }
    }

    private void dbuploader(DocumentReference documentReference, PostInfo postInfo){
        documentReference.set(postInfo)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "DocumentSnapshot successfully written!");
                    finish();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error writing document", e);
                }
            });
    }


    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void mystartActivity(Class c, int requestCode) {
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, requestCode);
    }

    private void NowPoint(){
        FirebaseFirestore pointDb = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        DocumentReference DR = pointDb.collection("users").document(user.getUid());
        DR.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    Object point = documentSnapshot.getData().get("point");
                    pointText.setText("잔여 포인트: " + point.toString() + "point");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,"Faild", e);
            }
        });
    }
    private void PlusPoint(){
        FirebaseFirestore pointDb = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        DocumentReference DR = pointDb.collection("users").document(user.getUid());
        DR.update("point", FieldValue.increment(500));
    }

}
