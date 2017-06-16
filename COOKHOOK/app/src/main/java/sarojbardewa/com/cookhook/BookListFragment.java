package sarojbardewa.com.cookhook;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

// This uses recycler view to display the list of books
public class BookListFragment extends Fragment {

    // TODO: Rename and change types of parameters
    private OnSelectedBookChangeListener mListener;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String[] mTitles;
    private int[] mImageResourceIds = {
            R.drawable.maryland_fried_chicken_with_creamy_gravy_tc,
            R.drawable.chicken_nuggets_tc,
            R.drawable.grilled_chicken_salad_wraps_tc,
            R.drawable.swiss_potato_breakfast_casserole_tc
    };

    //**** As per the suggestion from SO
    CoordinatorLayout.Behavior behavior;
    //***
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BookListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookListFragment newInstance() {
        BookListFragment fragment = new BookListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    public BookListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTitles = getResources().getStringArray(R.array.book_titles);
        mAdapter = new BookAdapter(mTitles, mImageResourceIds);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_book_card, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.book_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //Toast.makeText(getActivity(), mTitles[position], Toast.LENGTH_SHORT).show();
                        mListener.onSelectedBookChanged(view, position);
                    }
                }));

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i("FRAGMENT", "BookListFragment onAttach() called");

        try {
            mListener = (OnSelectedBookChangeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("FRAGMENT", "BookListFragment onDetach() called");
        mListener = null;
        //*************
    }

}
