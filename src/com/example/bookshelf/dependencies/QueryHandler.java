package com.example.bookshelf.dependencies;

import org.json.JSONException;

/**
 * Created by jls on 6/19/15.
 */
public class QueryHandler implements QueryCollector {

    private final DataBundler bundler;

    public QueryHandler(DataBundler bundler) {
        this.bundler = bundler;
    }

    @Override
    public int insert() {
        try {
            if (bundler.getOuterObject().getString(BookshelfConstants.RESULT_KEY_SUCCESS)
                    .equals(BookshelfConstants.RESULT_KEY_ONE) &&
                    bundler.getOuterObject().getString(BookshelfConstants.RESULT_KEY_QUERY)
                            .equals(BookshelfConstants.QUERY_INSERT)) {
                return 1;
            } else if (bundler.getOuterObject().getString(BookshelfConstants.RESULT_KEY_SUCCESS)
                    .equals(BookshelfConstants.RESULT_KEY_ZERO) &&
                    bundler.getOuterObject().getString(BookshelfConstants.RESULT_KEY_QUERY)
                            .equals(BookshelfConstants.QUERY_INSERT)) {
                return 0;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int select() {
        try {
            if (bundler.getOuterObject().getString(BookshelfConstants.RESULT_KEY_SUCCESS)
                    .equals(BookshelfConstants.RESULT_KEY_ONE)
                    && bundler.getOuterObject().getString(BookshelfConstants.RESULT_KEY_QUERY)
                    .equals(BookshelfConstants.QUERY_SELECT)) {
                return 1;
            } else if (bundler.getOuterObject().getString(BookshelfConstants.RESULT_KEY_SUCCESS)
                    .equals(BookshelfConstants.RESULT_KEY_ZERO) &&
                    bundler.getOuterObject().getString(BookshelfConstants.RESULT_KEY_QUERY)
                            .equals(BookshelfConstants.QUERY_SELECT)) {
                return 0;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int update() {
        try {
            if (bundler.getOuterObject().getString(BookshelfConstants.RESULT_KEY_SUCCESS)
                    .equals(BookshelfConstants.RESULT_KEY_ONE)
                    && bundler.getOuterObject().getString(BookshelfConstants.RESULT_KEY_QUERY)
                    .equals(BookshelfConstants.QUERY_UPDATE)) {
                return 1;
            } else if (bundler.getOuterObject().getString(BookshelfConstants.RESULT_KEY_SUCCESS)
                    .equals(BookshelfConstants.RESULT_KEY_ZERO) &&
                    bundler.getOuterObject().getString(BookshelfConstants.RESULT_KEY_QUERY)
                            .equals(BookshelfConstants.QUERY_UPDATE)) {
                return 0;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int delete() {
        try {
            if (bundler.getOuterObject().getString(BookshelfConstants.RESULT_KEY_SUCCESS)
                    .equals(BookshelfConstants.RESULT_KEY_ONE)
                    && bundler.getOuterObject().getString(BookshelfConstants.RESULT_KEY_QUERY)
                    .equals(BookshelfConstants.QUERY_DELETE)) {
                return 1;
            } else if (bundler.getOuterObject().getString(BookshelfConstants.RESULT_KEY_SUCCESS)
                    .equals(BookshelfConstants.RESULT_KEY_ZERO) &&
                    bundler.getOuterObject().getString(BookshelfConstants.RESULT_KEY_QUERY)
                            .equals(BookshelfConstants.QUERY_DELETE)) {
                return 0;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
