package mx.com.sportsworld.sw.model;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.util.Date;

/**
 * The Class NewsArticle.
 */
public class NewsArticle {

    /** The m permanent. */
    private final boolean mPermanent;
    
    /** The m author name. */
    private final String mAuthorName;
    
    /** The m author id. */
    private final long mAuthorId;
    
    /** The m un id. */
    private final long mUnId;
    
    /** The m image url. */
    private final String mImageUrl;
    
    /** The m content. */
    private final String mContent;
    
    /** The m available today. */
    private final boolean mAvailableToday;
    
    /** The m server id. */
    private final long mServerId;
    
    /** The m end of availability. */
    private final String mEndOfAvailability;
    
    /** The m resume. */
    private final String mResume;
    
    /** The m start of availability. */
    private final long mStartOfAvailability;
    
    /** The m category id. */
    private final long mCategoryId;
    
    /** The m category name. */
    private final String mCategoryName;
    
    /** The m title. */
    private final String mTitle;
    
    /**
	 * Instantiates a new news article.
	 * 
	 * @param permanent
	 *            the permanent
	 * @param authorName
	 *            the author name
	 * @param authorId
	 *            the author id
	 * @param idUn
	 *            the id un
	 * @param imageUrl
	 *            the image url
	 * @param content
	 *            the content
	 * @param availableToday
	 *            the available today
	 * @param serverId
	 *            the server id
	 * @param endOfAvailability
	 *            the end of availability
	 * @param resume
	 *            the resume
	 * @param startOfAvailability
	 *            the start of availability
	 * @param categoryId
	 *            the category id
	 * @param categoryName
	 *            the category name
	 * @param title
	 *            the title
	 */
    public NewsArticle(boolean permanent, String authorName, long authorId, long idUn,
            String imageUrl, String content, boolean availableToday, long serverId,
            String endOfAvailability, String resume, Date startOfAvailability, long categoryId,
            String categoryName, String title) {
        mPermanent = permanent;
        mAuthorName = authorName;
        mAuthorId = authorId;
        mUnId = idUn;
        mImageUrl = imageUrl;
        mContent = content;
        mAvailableToday = availableToday;
        mServerId = serverId;
        mEndOfAvailability = endOfAvailability;
        mResume = resume;
        mStartOfAvailability = startOfAvailability.getTime();
        mCategoryId = categoryId;
        mCategoryName = categoryName;
        mTitle = title;
    }

    /**
	 * Checks if is permanent.
	 * 
	 * @return true, if is permanent
	 */
    public boolean isPermanent() {
        return mPermanent;
    }

    /**
	 * Gets the author name.
	 * 
	 * @return the author name
	 */
    public String getAuthorName() {
        return mAuthorName;
    }

    /**
	 * Gets the author id.
	 * 
	 * @return the author id
	 */
    public long getAuthorId() {
        return mAuthorId;
    }

    /**
	 * Gets the un id.
	 * 
	 * @return the un id
	 */
    public long getUnId() {
        return mUnId;
    }

    /**
	 * Gets the image url.
	 * 
	 * @return the image url
	 */
    public String getImageUrl() {
        return mImageUrl;
    }

    /**
	 * Gets the content.
	 * 
	 * @return the content
	 */
    public String getContent() {
        return mContent;
    }

    /**
	 * Checks if is available today.
	 * 
	 * @return true, if is available today
	 */
    public boolean isAvailableToday() {
        return mAvailableToday;
    }

    /**
	 * Gets the server id.
	 * 
	 * @return the server id
	 */
    public long getServerId() {
        return mServerId;
    }

    /**
	 * Gets the end of availability.
	 * 
	 * @return the end of availability
	 */
    public String getEndOfAvailability() {
        return mEndOfAvailability;
    }

    /**
	 * Gets the resume.
	 * 
	 * @return the resume
	 */
    public String getResume() {
        return mResume;
    }

    /**
	 * Gets the start of availability.
	 * 
	 * @return the start of availability
	 */
    public long getStartOfAvailability() {
        return mStartOfAvailability;
    }

    /**
	 * Gets the category id.
	 * 
	 * @return the category id
	 */
    public long getCategoryId() {
        return mCategoryId;
    }

    /**
	 * Gets the category name.
	 * 
	 * @return the category name
	 */
    public String getCategoryName() {
        return mCategoryName;
    }

    /**
	 * Gets the title.
	 * 
	 * @return the title
	 */
    public String getTitle() {
        return mTitle;
    }

}
