package com.example.converter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;


public class ConverterActivity extends AppCompatActivity
{
    private ConverterViewModel _convertViewModel;
    private Spinner [] _categoryObjects;
    private TextView [] _data;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);
        _convertViewModel = ViewModelProviders.of(this).get(ConverterViewModel.class);
        _categoryObjects = new Spinner[]{ findViewById(R.id.inputCategoryObject), findViewById(R.id.outputCategoryObject) };
        _data = new TextView[]{ findViewById(R.id.input), findViewById(R.id.output) };
    }

    public void copyInput(View view)
    {
        ((ClipboardManager)getBaseContext().getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("", _data[0].getText()));
    }

    public void copyOutput(View view)
    {
        ((ClipboardManager)getBaseContext().getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("", _data[1].getText()));
    }

    public void Swap(View view)
    {
        int temp = (_categoryObjects[0].getSelectedItemPosition());
        _categoryObjects[0].setSelection(_categoryObjects[1].getSelectedItemPosition());
        _categoryObjects[1].setSelection(temp);
        _convertViewModel.Swap();
        onButtonClick(view);
    }

    public String getCoefficient(String inputCategory, String outputCategory)
    {
        return (getString(getResources().getIdentifier(inputCategory.concat(outputCategory).replaceAll("\\s",""), "string", getPackageName())));
    }

    public void onButtonClick(View view)
    {
        _convertViewModel.setCoefficient(getCoefficient(_categoryObjects[0].getSelectedItem().toString(), _categoryObjects[1].getSelectedItem().toString()));
        _convertViewModel.Convert();
    }
}