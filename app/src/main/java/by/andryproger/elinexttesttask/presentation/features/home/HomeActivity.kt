package by.andryproger.elinexttesttask.presentation.features.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.andryproger.elinexttesttask.R
import kotlinx.android.synthetic.main.activity_main.*

class HomeActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels { HomeVMFactory() }

    private val adapter get() = recyclerView.adapter as MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()
        subscribeToUpdates()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_add -> {
            viewModel.modelSources.addNewOne()
            true
        }
        R.id.action_reload_all -> {
            viewModel.modelSources.reloadAll()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager =
            object : StaggeredGridLayoutManager(GRID_ROWS, HORIZONTAL) {
                override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
                    lp?.width = width / GRID_COLUMNS
                    return true
                }
            }

        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = MainAdapter()
    }

    private fun subscribeToUpdates() {
        with(viewModel.modelSources) {
            val owner = this@HomeActivity

            items.observe(owner, Observer { items ->
                adapter.update(items)
            })

            loading.observe(owner, Observer { loading ->
                if (loading) {
                    supportActionBar?.setSubtitle(R.string.loading)
                } else {
                    supportActionBar?.subtitle = ""
                }
            })

            scrollToEnd.observe(owner, Observer { scrollToPos ->
                scrollToPos?.let {
                    recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
                    scrollToEnd.value = null
                }
            })

            error.observe(owner, Observer { errorState ->
                errorState?.let { e ->
                    if (!e.shown) {
                        Toast.makeText(owner, errorState.message, Toast.LENGTH_SHORT).show()
                        error.value = ErrorState(errorState.message, true)
                    }
                }
            })
        }
    }

    companion object {
        const val GRID_ROWS = 10
        const val GRID_COLUMNS = 7
    }
}