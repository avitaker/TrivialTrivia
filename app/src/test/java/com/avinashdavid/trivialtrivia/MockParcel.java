package com.avinashdavid.trivialtrivia;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import java.util.LinkedList;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyByte;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.booleanThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockParcel {

    @NonNull
    public static Parcel obtain() {
        return new MockParcel().mParcel;
    }

    private int mPosition = 0;
    private List<Object> mStore = new LinkedList<>();
    private Parcel mParcel = mock(Parcel.class);

    private MockParcel() {
        setupWrites();
        setupReads();
        setupOthers();
    }

    private void setupWrites() {
        final Answer<Object> answer = new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock i) throws Throwable {
                final Object arg = i.getArgument(0);
                mStore.add(arg);
                return arg;
            }
        };
        doAnswer(answer).when(mParcel).writeByte(anyByte());
        doAnswer(answer).when(mParcel).writeInt(anyInt());
        doAnswer(answer).when(mParcel).writeString(anyString());
        doAnswer(answer).when(mParcel).writeParcelable( any(Parcelable.class), anyInt() );
        doAnswer(answer).when(mParcel).writeFloat(anyFloat());
        doAnswer(answer).when(mParcel).writeList( anyList() );
    }

    private void setupReads() {
        final Answer<Object> answer = new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock i) throws Throwable {
                return mStore.get(mPosition++);
            }
        };
        when(mParcel.readByte()).thenAnswer(answer);
        when(mParcel.readInt()).thenAnswer(answer);
        when(mParcel.readString()).thenAnswer(answer);
        when(mParcel.readParcelable(any(ClassLoader.class))).then(answer);
        when(mParcel.readFloat()).thenAnswer(answer);
        when( mParcel.readArrayList(any(ClassLoader.class))).then(answer);
    }

    private void setupOthers() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock i) throws Throwable {
                mPosition = i.getArgument(0);
                return null;
            }
        }).when(mParcel).setDataPosition(anyInt());
    }

}