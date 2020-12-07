package com.example.converter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConverterViewModel extends ViewModel
{
    private MutableLiveData<String> _input;
    private MutableLiveData<String> _output;
    private MutableLiveData<String> _coefficient;

    public ConverterViewModel()
    {
        _input = new MutableLiveData<>();
        _output = new MutableLiveData<>();
        _coefficient = new MutableLiveData<>();
    }

    public MutableLiveData<String> getInput()
    {
        return this._input;
    }

    public void setInput(String input)
    {
        if (_input.getValue() == null)
        {
            _input.setValue("");
        }
        if ((_input.getValue().equals("0") && !input.equals(".")) && !(_input.getValue().length()>=2))
        {
            _input.setValue("");
        }
        if (_input.getValue().contains(".") && input.equals("."))
        {
            input = "";
        }
        _input.setValue(_input.getValue() + input);
    }

    public MutableLiveData<String> getOutput()
    {
        return _output;
    }

    public void setOutput(String output)
    {
        _output.setValue(output);
    }

    public MutableLiveData<String> getCoefficient()
    {
        return _coefficient;
    }

    public void setCoefficient(String coefficient)
    {
        _coefficient.setValue(coefficient);
    }

    public void Convert()
    {
        if (_input.getValue() != null)
        {
            _output.setValue(Float.toString(Float.parseFloat(_input.getValue()) * Float.parseFloat(_coefficient.getValue())));
        }
    }

    public void Reset()
    {
        _input.setValue("0");
        _output.setValue("0");
    }

    public void Swap()
    {
        _input.setValue(_output.getValue());
        Convert();
    }
}
