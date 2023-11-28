package me.dev.todoapp.screen.task.dialog.datetimepicker

import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.GravityCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import me.dev.todoapp.R
import me.dev.todoapp.data.base.model.Task
import me.dev.todoapp.databinding.DialogDatetimePickerBinding
import me.dev.todoapp.utils.CallbackResult
import java.time.Duration
import java.util.Calendar
import java.util.Date

class DatetimePickerDialogFragment : DialogFragment() {

    private var _binding: DialogDatetimePickerBinding? = null
    private val binding get() = _binding!!

    private var listener: IDatetimePickerListener? = null

    private val calendar: MutableLiveData<Calendar?> = MutableLiveData<Calendar?>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogDatetimePickerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        calendar.observe(this) {
            if (it == null) {
                binding.dialogDatetimePickerTimeTxt.text = getString(R.string.none)
                binding.dialogDatetimePickerNoDateBtn.isChecked = true
            } else {
                val currentCalendar = Calendar.getInstance()
                when (Duration.between(currentCalendar.toInstant(), it.toInstant()).toDays()) {
                    0L -> {
                        if (!binding.dialogDatetimePickerTodayBtn.isChecked)
                            binding.dialogDatetimePickerTodayBtn.isChecked = true
                    }

                    1L -> {
                        if (!binding.dialogDatetimePickerTomorrowBtn.isChecked)
                            binding.dialogDatetimePickerTomorrowBtn.isChecked = true
                    }

                    3L -> {
                        if (!binding.dialogDatetimePicker3DaysLaterBtn.isChecked)
                            binding.dialogDatetimePicker3DaysLaterBtn.isChecked = true
                    }

                    7L -> {
                        if (!binding.dialogDatetimePickerNextWeekBtn.isChecked)
                            binding.dialogDatetimePickerNextWeekBtn.isChecked = true
                    }

                    else -> {
                        binding.dialogDatetimePickerChipGroup.clearCheck()
                    }
                }
                val time: String = String.format(
                    "%02d:%02d",
                    calendar.value?.get(Calendar.HOUR_OF_DAY),
                    calendar.value?.get(Calendar.MINUTE)
                )
                binding.dialogDatetimePickerTimeTxt.text = time

                if (it.time.time != binding.dialogDatetimePickerCalendar.date) {
                    binding.dialogDatetimePickerCalendar.date =
                        calendar.value?.time?.time ?: Date().time
                }
            }
        }
        binding.dialogDatetimePickerCalendar.setOnDateChangeListener { _, year, monthOfYear, dayOfMonth ->
            val newCalendar: Calendar = calendar.value ?: Calendar.getInstance()
            newCalendar.set(year, monthOfYear, dayOfMonth)
            calendar.value = newCalendar
        }

        binding.dialogDatetimePickerNoDateBtn.setOnClickListener { calendar.value = null }
        binding.dialogDatetimePickerTodayBtn.setOnClickListener {
            calendar.value = Calendar.getInstance()
        }
        binding.dialogDatetimePickerTomorrowBtn.setOnClickListener {
            calendar.value = Calendar.getInstance().also { it.roll(Calendar.DATE, 1) }
        }
        binding.dialogDatetimePicker3DaysLaterBtn.setOnClickListener {
            calendar.value = Calendar.getInstance().also { it.roll(Calendar.DATE, 3) }
        }
        binding.dialogDatetimePickerNextWeekBtn.setOnClickListener {
            calendar.value = Calendar.getInstance().also { it.roll(Calendar.DATE, 7) }
        }

        binding.dialogDatetimePickerTimeBtn.setOnClickListener {
            val currentCalendar = Calendar.getInstance()
            TimePickerDialog(
                context,
                { _, hourOfDay, minute ->
                    if (calendar.value == null) return@TimePickerDialog
                    calendar.value?.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.value?.set(Calendar.MINUTE, minute)
                },
                currentCalendar.get(Calendar.HOUR_OF_DAY),
                currentCalendar.get(Calendar.MINUTE),
                true
            ).show()
        }

        binding.dialogDatetimePickerRepeatBtn.setOnClickListener {
            val popupMenu =
                PopupMenu(context, binding.dialogDatetimePickerRepeatTxt, GravityCompat.END).also {
                    it.menu.add(getString(R.string.none))
                    it.menu.add(Task.REPEAT_DAILY)
                    it.menu.add(Task.REPEAT_WEEKLY)
                    it.menu.add(Task.REPEAT_MONTHLY)
                    it.menu.add(Task.REPEAT_YEARLY)
                }
            popupMenu.setOnMenuItemClickListener {
                binding.dialogDatetimePickerRepeatTxt.text = it.title
                true
            }
            popupMenu.show()
        }

        binding.dialogDatetimePickerCancelBtn.setOnClickListener { dismiss() }
        binding.dialogDatetimePickerDoneBtn.setOnClickListener { dismiss() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDismiss(dialog: DialogInterface) {
        val repeat: String? =
            if (binding.dialogDatetimePickerRepeatTxt.text == getString(R.string.none)) {
                null
            } else {
                binding.dialogDatetimePickerRepeatTxt.text.toString()
            }

        val date: Date? =
            if (calendar.value == null) {
                null
            } else {
                calendar.value?.time
            }

        if (date == null) listener?.onFinish(CallbackResult.FAILED, null, null)
        else listener?.onFinish(CallbackResult.SUCCESS, date, repeat)
        super.onDismiss(dialog)
    }

    companion object {
        fun newInstance(listener: IDatetimePickerListener) = DatetimePickerDialogFragment().also {
            it.dialog?.setCanceledOnTouchOutside(true)
            it.listener = listener
        }
    }
}