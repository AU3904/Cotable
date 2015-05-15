package tk.wlemuel.cotable.activity.blog.fragment;

import android.view.View;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import tk.wlemuel.cotable.activity.blog.adapter.BlogAdapter;
import tk.wlemuel.cotable.api.BlogApi;
import tk.wlemuel.cotable.base.BaseRecycleAdapter;
import tk.wlemuel.cotable.base.BaseRecycleViewFragment;
import tk.wlemuel.cotable.core.AppContext;
import tk.wlemuel.cotable.model.Blog;
import tk.wlemuel.cotable.model.BlogList;
import tk.wlemuel.cotable.model.ListEntity;
import tk.wlemuel.cotable.utils.TLog;
import tk.wlemuel.cotable.utils.UIHelper;

/**
 * BlogListFragment
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc BlogListFragment
 * @created 2015/05/08
 * @updated 2015/05/08
 */
public class BlogListFragment extends BaseRecycleViewFragment {

    protected static final String TAG = BlogListFragment.class.getSimpleName();

    private static final String CACHE_KEY_PREFIX = "BLOG_LIST";

    // The max cache time set to be 12 hours
    private static final long MAX_CACAHE_TIME = 12 * 3600 * 1000;

    @Override
    protected boolean requestDataFromNetWork() {
        TLog.log(TAG, "requestDataFromNetwork");
        return System.currentTimeMillis()
                - AppContext.getRefreshTime(getCacheKey()) > MAX_CACAHE_TIME;
    }

    @Override
    protected BaseRecycleAdapter getListAdapter() {
        return new BlogAdapter();
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX;
    }

    @Override
    protected ListEntity parseList(String data) throws Exception {
        return BlogList.parse(data);
    }

    @Override
    protected ListEntity readList(Serializable seri) {
        return ((BlogList) seri);
    }

    @Override
    protected void sendRequestData() {
        if (mCatalog == BlogList.CATALOG_RECOMMEND) {
            BlogApi.getRecommendBlogList(mCurrentPage + 1, getResponseHandler());
        } else {
            BlogApi.getBlogList(mCurrentPage + 1, getResponseHandler());
        }
    }

    @Override
    public void onItemClick(View v, int position) {
        Blog blog = (Blog) mAdapter.getItem(position);

        Map<String, String> blogInfo = new HashMap<>();
        blogInfo.put(BlogDetailFragment.BUNDLE_KEY_BLOG_AUTHOR, blog.getAuthor_name());
        blogInfo.put(BlogDetailFragment.BUNDLE_KEY_BLOG_TITLE, blog.getTitle());
        blogInfo.put(BlogDetailFragment.BUNDLE_KEY_BLOG_POSTED, blog.getUpdated());
        blogInfo.put(BlogDetailFragment.BUNDLE_KEY_BLOG_READS, blog.getReads() == 0 ? "0" : Integer
                .toString(blog.getReads()));
        blogInfo.put(BlogDetailFragment.BUNDLE_KEY_BLOG_ID, blog.getPostId());


        if (blog != null) {
            UIHelper.showBlogDetail(getActivity(), blogInfo);
        }
    }


}
