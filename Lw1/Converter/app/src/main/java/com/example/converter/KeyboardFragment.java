package com.example.converter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class KeyboardFragment extends Fragment
{
    private ConverterViewModel _convertViewModel;
    private Button [] _keyboard;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.keyboard_fragment, container, false);
        _keyboard = new Button[]{ view.findViewById(R.id.key0), view.findViewById(R.id.key1), view.findViewById(R.id.key2),
                                  view.findViewById(R.id.key3), view.findViewById(R.id.key4), view.findViewById(R.id.key5),
                                  view.findViewById(R.id.key6), view.findViewById(R.id.key7), view.findViewById(R.id.key8),
                                  view.findViewById(R.id.key9), view.findViewById(R.id.keyDot), view.findViewById(R.id.keyReset) };
        for (int i = 0; i < (_keyboard.length - 1); i++)
        {
            int j = i;
            _keyboard[i].setOnClickListener(item -> _convertViewModel.setInput(keyToString(_keyboard[j])));
        }
        _keyboard[11].setOnClickListener(item -> _convertViewModel.Reset());
        return view;
    }

    static String keyToString(Button key)
    {
        return key.getText().toString();
    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        _convertViewModel = ViewModelProviders.of(getActivity()).get(ConverterViewModel.class);
    }
}
