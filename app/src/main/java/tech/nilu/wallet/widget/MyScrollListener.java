package tech.nilu.wallet.widget;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class MyScrollListener extends RecyclerView.OnScrollListener {
    private final int fetchSize;
    private final LinearLayoutManager manager;

    private int currentPage = 0;
    private int previousTotal = 0;
    private boolean loadMore = true;
    private boolean isLoading = true;

    public MyScrollListener(LinearLayoutManager manager, int fetchSize) {
        this.manager = manager;
        this.fetchSize = fetchSize;
    }

    public void init() {
        this.currentPage = 0;
        this.loadMore = true;
        this.isLoading = true;
        this.previousTotal = 0;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = manager.getItemCount();
        int firstVisibleItem = manager.findFirstVisibleItemPosition();

        if (isLoading && totalItemCount > previousTotal) {
            isLoading = false;
            previousTotal = totalItemCount;
        }
        if (!isLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + 1)) {
            currentPage += fetchSize;
            if (loadMore) onLoadMore(currentPage);
            isLoading = true;
        }
    }

    public void setLoadMore(boolean loadMore) {
        this.loadMore = loadMore;
    }

    public abstract void onLoadMore(int currentPage);
}
