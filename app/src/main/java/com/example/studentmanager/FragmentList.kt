    package com.example.studentmanager

    import android.os.Bundle
    import android.util.Log
    import android.view.ContextMenu
    import android.view.LayoutInflater
    import android.view.Menu
    import android.view.MenuInflater
    import android.view.MenuItem
    import android.view.View
    import android.view.ViewGroup
    import android.widget.AdapterView
    import android.widget.ListView
    import androidx.fragment.app.Fragment
    import androidx.fragment.app.activityViewModels
    import androidx.navigation.fragment.findNavController
    import com.example.studentmanager.StudentAdapter
    import com.example.studentmanager.StudentViewModel

    class FragmentList : Fragment() {
        private lateinit var adapter: StudentAdapter
        private val studentViewModel: StudentViewModel by activityViewModels()


        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            setHasOptionsMenu(true)
            val view = inflater.inflate(R.layout.list_student, container, false)
            val listView = view.findViewById<ListView>(R.id.list_view_students)

            adapter = StudentAdapter(requireContext(), studentViewModel.students.value ?: mutableListOf())
            listView.adapter = adapter

            registerForContextMenu(listView)

            studentViewModel.students.observe(viewLifecycleOwner) { updatedList ->

                Log.d("FragmentList", "Updated List: $updatedList")
                adapter.updateData(updatedList)
            }
            listView.onItemClickListener = null
            return view
        }


        override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            inflater.inflate(R.menu.option_menu, menu)
            super.onCreateOptionsMenu(menu, inflater)
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.add_new -> {
                    val action = FragmentListDirections.actionStudentListToAddEditStudent(
                        studentName = null,
                        studentId = null,
                        position = -1
                    )
                    findNavController().navigate(action)
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }

        override fun onCreateContextMenu(
            menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            super.onCreateContextMenu(menu, v, menuInfo)
            requireActivity().menuInflater.inflate(R.menu.context_menu, menu)
        }
        override fun onContextItemSelected(item: MenuItem): Boolean {
            val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
            val selectedStudent = studentViewModel.students.value?.get(info.position)

            return when (item.itemId) {
                R.id.edit -> {
                    if (selectedStudent != null) {
                        val action = FragmentListDirections.actionStudentListToAddEditStudent(
                            studentName = selectedStudent.studentName,
                            studentId = selectedStudent.studentId,
                            position = info.position
                        )
                        Log.d("FragmentList", "Navigating to Add/Edit Fragment")
                        findNavController().navigate(action)
                    }
                    true
                }
                R.id.delete -> {
                    studentViewModel.students.value?.let {
                        val updatedList = it.toMutableList()
                        updatedList.removeAt(info.position)
                        studentViewModel.students.value = updatedList
                    }
                    true
                }
                else -> super.onContextItemSelected(item)
            }
        }

    }

