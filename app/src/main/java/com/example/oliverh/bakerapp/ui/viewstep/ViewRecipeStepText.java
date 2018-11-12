package com.example.oliverh.bakerapp.ui.viewstep;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oliverh.bakerapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewRecipeStepText.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewRecipeStepText#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewRecipeStepText extends Fragment {
    public static final String ARG_STEP_HEADER = "header";
    public static final String ARG_STEP_DESC = "desc";
    private String mHeader;
    private String mDesc;
    private OnFragmentInteractionListener mListener;

    @BindView(R.id.tv_recipeStepHeader)
    @Nullable
    TextView recipeStepHeader;

    @BindView(R.id.tv_recipeStepDesc)
    @Nullable
    TextView recipeStepDescription;

    public ViewRecipeStepText() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param header Parameter 1.
     * @param desc   Parameter 2.
     * @return A new instance of fragment ViewRecipeStepText.
     */
    public static ViewRecipeStepText newInstance(String header, String desc) {
        ViewRecipeStepText fragment = new ViewRecipeStepText();
        Bundle args = new Bundle();
        args.putString(ARG_STEP_HEADER, header);
        args.putString(ARG_STEP_DESC, desc);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mHeader = getArguments().getString(ARG_STEP_HEADER);
            mDesc = getArguments().getString(ARG_STEP_DESC);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_recipe_step_text, container, false);
        ButterKnife.bind(this, view);

        recipeStepHeader.setText(mHeader);
        recipeStepDescription.setText(mDesc);

        return view;
    }

    // TODO: Implement nextStep UI action
    @OnClick(R.id.btn_nextStep)
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.OnNextStepFragmentInteraction();
        }
    }

    public void updateFragmentUI() {
        if (getArguments() != null) {
            mHeader = getArguments().getString(ARG_STEP_HEADER);
            mDesc = getArguments().getString(ARG_STEP_DESC);

            recipeStepHeader.setText(mHeader);
            recipeStepDescription.setText(mDesc);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void OnNextStepFragmentInteraction();
    }
}
