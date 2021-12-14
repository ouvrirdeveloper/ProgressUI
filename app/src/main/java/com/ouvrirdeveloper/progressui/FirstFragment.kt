package com.ouvrirdeveloper.progressui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.ouvrirdeveloper.progressui.databinding.FragmentFirstBinding
import kotlinx.coroutines.delay

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private var status = MutableLiveData<Int>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        status.observe(viewLifecycleOwner, {
            when (it) {
                1 -> (requireActivity() as BaseActivity).showProgress(backgroundColorRes = R.color.teal_200)
                2 -> (requireActivity() as BaseActivity).showProgress(backgroundColorRes = R.color.black,message = "please wait it will take one more moment")
                3 -> (requireActivity() as BaseActivity).showProgress(showRetry = true, reTry = {
                    (requireActivity() as BaseActivity)
                })
                4 -> (requireActivity() as BaseActivity).hideProgress()
            }
        })
        binding.buttonFirst.setOnClickListener {
            lifecycleScope.launchWhenStarted {
                for (i in 1..4) {
                    status.value = i
                    delay(4000)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}