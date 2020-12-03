package com.example.converter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;


public class MainFragment extends Fragment
{
    private ConverterViewModel _convertViewModel;
    private Spinner [] _categoryObjects;
    private TextView [] _data;
    private int _resource;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle sevedInstanceState)
    {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        _convertViewModel = ViewModelProviders.of(getActivity()).get(ConverterViewModel.class);
        _resource = ((getResources().getIdentifier((getActivity().getIntent().getExtras().get("Category")).toString().toLowerCase(), "array", getContext().getPackageName())));
        _categoryObjects = new Spinner[]{ view.findViewById(R.id.inputCategoryObject), view.findViewById(R.id.outputCategoryObject) };
        _data = new TextView[]{ view.findViewById(R.id.input), view.findViewById(R.id.output) };
        initializeSpinner(_categoryObjects[0], _resource);
        initializeSpinner(_categoryObjects[1], _resource);
        _convertViewModel.getInput().observe(requireActivity(), x -> _data[0].setText(x));
        _convertViewModel.getOutput().observe(requireActivity(), x -> _data[1].setText(x));
        return view;
    }

    private void initializeSpinner(Spinner spn, int resource)
    {
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getContext(), resource, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adapter);
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View itemSelected, int selectedItemPosition, long selectedId)
            {
                    }
                    public void onNothingSelected(AdapterView<?> parent)
                    {
                    }
        });
    }
}
