package com.diegulog.ticket.emvnfc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.devnied.emvnfccard.exception.CommunicationException;
import com.github.devnied.emvnfccard.model.EmvCard;
import com.github.devnied.emvnfccard.parser.EmvTemplate;
import com.github.devnied.emvnfccard.parser.IProvider;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        IsoDep mIsoDep = IsoDep.get(tag);
        readCard(mIsoDep);
    }

    private void readCard(final IsoDep mTagCom) {
        // Create provider
        IsoDepProvider provider = new IsoDepProvider(mTagCom);
        EmvTemplate.Config config = EmvTemplate.Config()
                .setContactLess(true) // Enable contact less reading (default: true)
                .setReadAllAids(true) // Read all aids in card (default: true)
                .setReadTransactions(true) // Read all transactions (default: true)
                .setReadCplc(false) // Read and extract CPCLC data (default: false)
                .setRemoveDefaultParsers(false) // Remove default parsers for GeldKarte and EmvCard (default: false)
                .setReadAt(true) // Read and extract ATR/ATS and description
                ;
        EmvTemplate parser = EmvTemplate.Builder() //
                .setProvider(provider) // Define provider
                .setConfig(config) // Define config
                //.setTerminal(terminal) (optional) you can define a custom terminal implementation to create APDU
                .build();

        try {
            provider.connect();
            EmvCard card = parser.readEmvCard();
            provider.disconnect();
            TextView cardNumber = findViewById(R.id.card_number);
            cardNumber.setText(card.toString());
            Log.d("MainActivity", "card: Number" +card.getCardNumber());
        } catch (CommunicationException e) {
            e.printStackTrace();
        }
    }
}