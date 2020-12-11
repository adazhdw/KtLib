package com.adazhdw.libapp.ui.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.adazhdw.kthttp.KtHttp
import com.adazhdw.kthttp.coroutines.toClazz
import com.adazhdw.ktlib.widget.list.BaseVBViewHolder
import com.adazhdw.ktlib.widget.list.ListFragment
import com.adazhdw.ktlib.widget.list.LoadMoreAdapter
import com.adazhdw.libapp.bean.ListResponse
import com.adazhdw.libapp.bean.WxArticleChapter
import com.adazhdw.libapp.databinding.NetChapterItemBinding

/**
 * author：daguozhu
 * date-time：2020/12/10 17:17
 * description：
 **/

class WxChaptersFragment : ListFragment<WxArticleChapter, ChaptersAdapter>() {
    override fun loadMoreAvailable(): Boolean = false

    /*
    // IM模式，最新消息在最底，通过下拉到顶部，加载历史消息
    override fun rvExtra(recyclerView: LoadMoreRecyclerViewEx) {
        recyclerView.canScrollDirection(LoadMoreRecyclerViewEx.SCROLL_DIRECTION_TOP)
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
    }*/

    override fun getDataAdapter(): ChaptersAdapter = ChaptersAdapter()

    override fun onLoad(page: Int, callback: LoadDataCallback<WxArticleChapter>) {
        launchOnUI {
            val url = "https://wanandroid.com/wxarticle/chapters/json"
            val data = KtHttp.ktHttp.get(url).toClazz<ListResponse<WxArticleChapter>>().await()
            callback.onSuccess(data.data ?: listOf(), true)
        }
    }
}

class ChaptersAdapter : LoadMoreAdapter<WxArticleChapter>() {

    override fun bindItemHolder(holder: BaseVBViewHolder, data: WxArticleChapter, position: Int) {
        (holder.viewBinding as NetChapterItemBinding).chapterName.text = data.chapterName
    }

    override fun getItemBinding(inflater: LayoutInflater, parent: ViewGroup): ViewBinding {
        return NetChapterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }
}



