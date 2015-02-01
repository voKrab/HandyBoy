package com.vallverk.handyboy.view.base;

import java.util.ArrayList;
import java.util.List;

public class Refresher
{
    protected int loadedItems;
    protected boolean hasMore;
    protected int pageLimit;

    public Refresher ( int pageLimit )
    {
        this.pageLimit = pageLimit;
    }

    public List < Object > refresh () throws Exception
    {
        return new ArrayList < Object > ();
    }

    public boolean isNeedLoadMore ( int totalItemCount )
    {
        return loadedItems != totalItemCount;
    }

    public void setHasMore ( boolean hasMore )
    {
        this.hasMore = hasMore;
    }

    public boolean isHasMore ()
    {
        return hasMore;
    }

    public List < Object > loadMoreItems ( int totalItemCount ) throws Exception
    {
        if ( isNeedLoadMore ( totalItemCount ) )
        {
            loadedItems = totalItemCount;
            List < Object > items = loadMoreItems ();
            setHasMore ( items.size () == pageLimit );
            return items;
        } else
        {
            return new ArrayList < Object > ();
        }
    }

    public List < Object > loadMoreItems () throws Exception
    {
        return new ArrayList < Object > ();
    }
}
