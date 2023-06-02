package com.betelgeuse.blockchain.data.firebase;

import androidx.annotation.NonNull;

import com.betelgeuse.blockchain.H;
import com.betelgeuse.blockchain.data.ADatabase;
import com.betelgeuse.blockchain.data.ICurrencyOperations;
import com.betelgeuse.blockchain.data.dataListener.CurrencyDTOListListener;
import com.betelgeuse.blockchain.data.dto.CurrencyDTO;
import com.betelgeuse.blockchain.data.dto.TickerDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CurrencyDB extends ADatabase implements ICurrencyOperations {
    private FirebaseFirestore db;
    private CollectionReference currencyCollection;
    public CurrencyDB() {
        super();
        this.db = FirebaseFirestore.getInstance();
        currencyCollection = db.collection(CurrencyDTO.class.getSimpleName());
    }


    @Override
    public void readCurrencies(CurrencyDTOListListener listener) {
        currencyCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<CurrencyDTO> currencyDTOList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                              currencyDTOList.add(  document.toObject(CurrencyDTO.class));
                    }
                    listener.onSuccess(currencyDTOList);
                }
            }
        });
    }
}
