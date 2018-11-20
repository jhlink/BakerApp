package com.example.oliverh.bakerapp.ui.viewstep;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.oliverh.bakerapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewRecipeStepTextFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewRecipeStepTextFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewRecipeStepTextFragment extends Fragment {
    public static final String ARG_STEP_HEADER = "header";
    public static final String ARG_STEP_DESC = "desc";
    public static final String ARG_IS_NEXT_BTN_VISIBLE = "isTablet";

    private String mHeader;
    private String mDesc;
    private boolean hideNextBtn;
    private OnFragmentInteractionListener mListener;

    @BindView(R.id.tv_recipeStepHeader)
    @Nullable
    TextView recipeStepHeader;

    @BindView(R.id.tv_recipeStepDesc)
    @Nullable
    TextView recipeStepDescription;

    @BindView(R.id.btn_nextStep)
    @Nullable
    Button nextStepBtn;


    public ViewRecipeStepTextFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param header Parameter 1.
     * @param desc   Parameter 2.
     * @return A new instance of fragment ViewRecipeStepTextFragment.
     */
    public static ViewRecipeStepTextFragment newInstance(String header, String desc, boolean
            shouldHideBtn) {
        ViewRecipeStepTextFragment fragment = new ViewRecipeStepTextFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STEP_HEADER, header);
        args.putString(ARG_STEP_DESC, desc);
        args.putBoolean(ARG_IS_NEXT_BTN_VISIBLE, shouldHideBtn);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mHeader = getArguments().getString(ARG_STEP_HEADER);
            mDesc = getArguments().getString(ARG_STEP_DESC);
            hideNextBtn = getArguments().getBoolean(ARG_IS_NEXT_BTN_VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_recipe_step_text, container, false);
        ButterKnife.bind(this, view);

        recipeStepHeader.setText(mHeader);
        recipeStepDescription.setText(mDesc);
        hideNextBtn(hideNextBtn);

        return view;
    }

    @OnClick(R.id.btn_nextStep)
    public void onButtonPressed() {
        if (!hideNextBtn && mListener != null) {
            mListener.OnNextStepFragmentInteraction();
        }
    }

    public void updateFragmentUI(Bundle bundle) {
        if (bundle != null) {
            mHeader = bundle.getString(ARG_STEP_HEADER);
            mDesc = bundle.getString(ARG_STEP_DESC);
            hideNextBtn = bundle.getBoolean(ARG_IS_NEXT_BTN_VISIBLE);

            recipeStepHeader.setText(mHeader);
            recipeStepDescription.setText(mDesc);
            hideNextBtn(hideNextBtn);
        }
    }

    private void hideNextBtn(boolean shouldBeHidden) {
        nextStepBtn.setVisibility(shouldBeHidden ? View.GONE : View.VISIBLE);
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
