package com.diegulog.ticket.emvnfc;

import android.nfc.tech.IsoDep;

import com.github.devnied.emvnfccard.exception.CommunicationException;
import com.github.devnied.emvnfccard.parser.IProvider;

import java.io.IOException;

/**
 * Created by Diego Guaña on 22/11/2022.
 */
public class IsoDepProvider implements IProvider {

    private final IsoDep mTagCom;

    public IsoDepProvider(IsoDep mTagCom) {
        this.mTagCom = mTagCom;
    }

    public void connect() {
        if (!mTagCom.isConnected()) {
            try {
                mTagCom.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void disconnect() {
        try {
            mTagCom.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] transceive(final byte[] pCommand) throws CommunicationException {

        byte[] response;
        try {
            // send command to emv card
            response = mTagCom.transceive(pCommand);
        } catch (IOException e) {
            throw new CommunicationException(e.getMessage());
        }

        return response;
    }

    @Override
    public byte[] getAt() {
        // For NFC-A
        return mTagCom.getHistoricalBytes();
        // For NFC-B
        // return mTagCom.getHiLayerResponse();
    }


}