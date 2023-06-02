package com.betelgeuse.blockchain.data.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.betelgeuse.blockchain.data.ADatabase;
import com.betelgeuse.blockchain.data.ITickerDB;
import com.betelgeuse.blockchain.data.dataListener.TickerDTOListListener;
import com.betelgeuse.blockchain.data.dataListener.TickerDTOListener;
import com.betelgeuse.blockchain.data.dto.TickerDTO;
import com.betelgeuse.blockchain.H;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SnapshotMetadata;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TickerDBSERVER extends ADatabase implements ITickerDB {
    Source sourceOfServer = Source.SERVER;
    private FirebaseFirestore db;
    private CollectionReference tickerCollection;

    public TickerDBSERVER(FirebaseFirestore db) {
        super();
        this.db = db;
        tickerCollection = db.collection(TickerDTO.class.getSimpleName());
    }

    @Override  // does work
    public void readTickerOfDateLIVE(String date, String toCurrency, TickerDTOListener listener) {
        Source source = Source.SERVER;
        /*----------------------------------------------------------------------------*/
        Date dateObj = parseStringDateToDateObject(date);
        String tickerId = this.baseCurrency + "-" + toCurrency;
        String year = String.valueOf(dateObj.getYear());
        DocumentReference docRef = tickerCollection.document(tickerId).collection(year).document(date);

        docRef.addSnapshotListener((snapshot, e) -> {
            SnapshotMetadata snapshotMetadata = snapshot.getMetadata();
            //  Log.d("TAG", new Gson().toJson(snapshotMetadata));
            boolean hasPendingWrites = snapshotMetadata.hasPendingWrites();
            String source1 = snapshot != null && hasPendingWrites ? "Local" : "Server";
            // bu noktada her veri degisikliginde geri bildirim alirsin bunu yapma!!! price da , High da Low da Candle da vs...
            if (e != null) {
                // Log.w("TAG", "Listen failed.", e);
                return;
            }

            if (snapshot != null && (!hasPendingWrites) && snapshot.exists()) {
                // Log.d("hasPendingWrites", "Current data: " + snapshot.getData());
                listener.onSuccess(snapshot.toObject(TickerDTO.class));
            } else {
                // Log.d("TAG", "Current data: null");
            }
            /*---------------------veriyi sadece bir defa almayı bu sekilde basarabildim!!---------------------*/
            TickerDTO tickerDTO = snapshot.toObject(TickerDTO.class);
            if (tickerDTO==null) return;
            if (tickerDTO.getPrice() < tickerDTO.getLow()) return;
            if (tickerDTO.getPrice() > tickerDTO.getHigh()) return;
            if (tickerDTO.getCandle() != (tickerDTO.getPrice() - tickerDTO.getOpen())) return;
            H.debugLog(this.getClass().getSimpleName(), "readTickerOfDateLIVE", snapshot.getData().toString());
            //   Log.d("hasPendingWrites", "Current data: " + snapshot.getData());


        });
        /*-----------------------------------------------------------------------------*/
    }

    private void detachListenerRegistration(String date, String toCurrency, TickerDTOListener listener) {
        Date dateObj = parseStringDateToDateObject(date);
        String tickerId = this.baseCurrency + "-" + toCurrency;
        String year = String.valueOf(dateObj.getYear());
        DocumentReference docRef = tickerCollection.document(tickerId).collection(year).document(date);
        ListenerRegistration listenerRegistration = docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

            }
        });
        listenerRegistration.remove();


    }

    private void readTickerOfDate_AttachAndDetachDataChangeListener(String date, String fromCurrency, String toCurrency, TickerDTOListener listener) {
        Source source = Source.SERVER;
        /*----------------------------*/
        Date dateObj = parseStringDateToDateObject(date);
        String tickerId = fromCurrency + "-" + toCurrency;
        String year = String.valueOf(dateObj.getYear());
        DocumentReference docRef =
                tickerCollection.document(tickerId).collection(year).document(date);
        ListenerRegistration registration =
                docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                        String source =
                                snapshot != null && snapshot.getMetadata().hasPendingWrites() ? "Local" : "Server"; // bu ne isimize yariyacak
                        // bu noktada her veri degisikliginde geri bildirim alirsin bunu yapma!!! price da , High da Low da Candle da vs...
                        if (e != null) {
                            Log.w("TAG", "Listen failed.", e);
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            Log.d("TAG", "Current data: " + snapshot.getData());
                            TickerDTO tickerDTO = snapshot.toObject(TickerDTO.class);
                            listener.onSuccess(tickerDTO);
                        } else {
                            Log.d("TAG", "Current data: null");
                        }
                    }
                });

        if (date != getDate_AsSimpleDateFormatString()) {
            registration.remove();
        }


        /*-----------------------------------------------------------------------------*/
    }
    @Override
    public void readTickersOfDate(String date, TickerDTOListListener listener) {
        Date dateObj = parseStringDateToDateObject(date);
        String year = String.valueOf(dateObj.getYear());
        H.debugLog(this.getClass().getSimpleName(), "readTickersOfDate", year);
        db.collectionGroup(year).whereEqualTo("calendar", date).get(sourceOfServer)
                .addOnSuccessListener((QuerySnapshot queryDocumentSnapshots) -> {
                    List<TickerDTO> tickerDTOList = new ArrayList<>();
                    //H.debugLog(this.getClass().getSimpleName(), "readTickersOfDate", "success!");
                    for (QueryDocumentSnapshot snap : queryDocumentSnapshots) {
                        tickerDTOList.add(snap.toObject(TickerDTO.class));
                        H.debugLog(this.getClass().getSimpleName(), "readTickersOfDate", snap.getData().toString());
                    }
                    if (listener != null) {
                        // H.debugLog(this.getClass().getSimpleName(), "readTickersOfDate", "tickerList size : " + String.valueOf(tickerDTOList.size()));
                        listener.onSuccess(tickerDTOList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        H.errorLog(this.getClass().getSimpleName(), "readTickersOfDate", e.getLocalizedMessage());
                    }
                });
    }

    public void readTickersFromDateToNow(String date,  TickerDTOListListener listener) {

        Date dateObj = parseStringDateToDateObject(date);
        String year = String.valueOf(dateObj.getYear());
        // collectionGroup için path index...
        db.collectionGroup(year).whereGreaterThan("calendar", date).get(sourceOfServer)
                .addOnSuccessListener((QuerySnapshot queryDocumentSnapshots) -> {
                            List<TickerDTO> tickerList = null;
                            H.debugLog(this.getClass().getSimpleName(), "readTickersFromDateToNow", "success!");
                            for (QueryDocumentSnapshot snap : queryDocumentSnapshots) {
                                tickerList.add(snap.toObject(TickerDTO.class));
                                H.debugLog(this.getClass().getSimpleName(), "readTickersFromDateToNow", snap.getData().toString());
                            }
                            if (listener != null) {
                                listener.onSuccess(tickerList);
                            }
                        }
                )
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        H.errorLog(this.getClass().getSimpleName(), "readTickersFromDateToNow", e.getLocalizedMessage());
                    }
                });
    }
    @Override
    public void readTickerOfDate(String toCurrency, String dateValue,  TickerDTOListener listener) {
        Date dateObj = parseStringDateToDateObject(dateValue);
        String tickerId = this.baseCurrency + "-" + toCurrency;
        String year = String.valueOf(dateObj.getYear());
        DocumentReference docRef =
                tickerCollection.document(tickerId).collection(year).document(dateValue);
        docRef.get(sourceOfServer).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        TickerDTO tickerDTO = document.toObject(TickerDTO.class);
                        if (listener != null) {
                            listener.onSuccess(tickerDTO);
                        }
                        H.debugLog(this.getClass().getSimpleName(), "readTickerOfDate", document.getData().toString());
                    } else {
                        Log.d("readTickerOfDate", "No such document");
                    }
                } else {
                    H.debugLog("readTickerOfDate", "get failed with ", task.getException().getLocalizedMessage());
                }
            }
        });
    }
    @Override
    public void readTickerFromDateToNow(String toCurrency, String fromDate,  TickerDTOListListener listener) {

        Date dateObj = parseStringDateToDateObject(fromDate);
        String fromYear = String.valueOf(dateObj.getYear());
        String toYear = getYearNowOfCurrentTimeZone();
        int fromYearINT = Integer.valueOf(fromYear);
        int toYearINT = Integer.valueOf(toYear);
        List<TickerDTO> returnTickerList = new ArrayList<>();

        do {
            int finalFromYearINT = fromYearINT;
            readTickerFromDateTo_EndOfTheYear(toCurrency, fromDate, new TickerDTOListListener() {
                @Override
                public void onSuccess(List<TickerDTO> tickerList) {
                    returnTickerList.addAll(tickerList);
                    if ((finalFromYearINT == toYearINT) && listener != null) {
                        listener.onSuccess(returnTickerList);
                    }

                }
            });



            /*---------------------------*/

            /*-------------------------*/


            fromYearINT = fromYearINT + 1;
            fromDate = fromYearINT + "-" + "01" + "-" + "01";

        } while (fromYearINT <= toYearINT);


    }

    /*--------------------------------*/
    private void readTickerFromDateTo_EndOfTheYear(String toCurrency, String fromDate,  TickerDTOListListener listener) {
        // does work ...
        String tickerId = (this.baseCurrency + "-" + toCurrency).toUpperCase();
        Date dateObj = parseStringDateToDateObject(fromDate);
        String yearSTRİNG = String.valueOf(dateObj.getYear());

        CollectionReference collRef = tickerCollection.document(tickerId).collection(yearSTRİNG);
        List<TickerDTO> tickerList = new ArrayList<>();
        collRef.whereGreaterThanOrEqualTo("calendar", fromDate).get(sourceOfServer).addOnCompleteListener((Task<QuerySnapshot> task) -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> dSList = task.getResult().getDocuments();
                H.debugLog(this.getClass().getSimpleName(), "readTickerFromDateTo_EndOfTheYear", String.valueOf(dSList.size()));
                for (DocumentSnapshot ds : dSList) {
                    TickerDTO tickerDTO = ds.toObject(TickerDTO.class);
                    tickerList.add(tickerDTO);
                    H.debugLog(this.getClass().getSimpleName(), "readTickerFromDateTo_EndOfTheYear: " + tickerDTO.getSymbol() + tickerDTO.getCalendar());
                }
                /*-----------returning tickers-------------------------*/
                if (listener != null) {
                    listener.onSuccess(tickerList);
                }
            } else {
                H.errorLog(this.getClass().getSimpleName(), "readTickerFromDateTo_EndOfTheYear", task.getException().getLocalizedMessage());
            }
        });


    }

}



