package com.st18rai.firebasechat.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.st18rai.firebasechat.R;
import com.st18rai.firebasechat.adapter.ChatRecyclerAdapter;
import com.st18rai.firebasechat.interfaces.Constants;
import com.st18rai.firebasechat.model.Chat;
import com.st18rai.firebasechat.model.User;
import com.st18rai.firebasechat.ui.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatFragment extends BaseFragment {
    @BindView(R.id.avatar)
    CircleImageView avatar;

    @BindView(R.id.user_name)
    TextView userName;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.message)
    EditText message;

    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private String userID;
    private ChatRecyclerAdapter adapter;
    private List<Chat> chatList;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            userID = getArguments().getString(Constants.USER_ID);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        showBackButton();
    }

    @Override
    public void onStop() {
        super.onStop();
        hideBackButton();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecycler();
        loadUserInfo(userID);

    }

    private void initRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new ChatRecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void loadUserInfo(String userID) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.USERS).child(userID);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                userName.setText(user.getUsername());

                if (!user.getImageURL().equals(Constants.DEFAULT)) {
                    Glide.with(getContext()).load(user.getImageURL()).into(avatar);
                }

                readMessages(firebaseUser.getUid(), userID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMessages(String myId, String userId) {

        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.CHATS);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Chat chat = snapshot.getValue(Chat.class);

                    if (chat.getReceiver().equals(myId) && chat.getSender().equals(userId) ||
                            chat.getReceiver().equals(userId) && chat.getSender().equals(myId)) {
                        chatList.add(chat);
                    }

                    adapter.setData(chatList);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendMessage(String sender, String receiver, String message) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(Constants.SENDER, sender);
        hashMap.put(Constants.RECEIVER, receiver);
        hashMap.put(Constants.MESSAGE, message);

        databaseReference.child(Constants.CHATS).push().setValue(hashMap);

    }

    @OnClick(R.id.button_send)
    void onSendButtonClick() {

        String msg = message.getText().toString();

        if (TextUtils.isEmpty(msg)) {
            Toast.makeText(getContext(), getResources().getString(R.string.enter_message_toast), Toast.LENGTH_SHORT).show();
        } else {
            sendMessage(firebaseUser.getUid(), userID, msg);
        }

        message.setText("");

    }


}
