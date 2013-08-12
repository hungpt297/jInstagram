package org.jinstagram.realtime;

import org.jinstagram.auth.model.OAuthConstants;
import org.jinstagram.auth.model.OAuthRequest;
import org.jinstagram.entity.common.InstagramErrorResponse;
import org.jinstagram.exceptions.InstagramException;
import org.jinstagram.http.Response;
import org.jinstagram.http.Verbs;
import org.jinstagram.model.QueryParam;
import org.jinstagram.utils.Preconditions;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

public class InstagramSubscription {
	private String aspect;

	private String callback;

	private String clientId;

	private String clientSecret;

	private String verifyToken;

	/**
	 * Default constructor
	 */
	public InstagramSubscription() {
		this.callback = OAuthConstants.OUT_OF_BAND;
	}

	/**
	 * Configures the callback url
	 *
	 * @param callback The callback for your application
	 * @return the {@link InstagramSubscription} instance for method chaining
	 */
	public InstagramSubscription callback(String callback) {
		Preconditions.checkValidUrl(callback, "Invalid Callback Url");

		this.callback = callback;

		return this;
	}

	/**
	 * Configures the clientId
	 *
	 * @param clientId The clientId for your application
	 * @return the {@link InstagramSubscription} instance for method chaining
	 */
	public InstagramSubscription clientId(String clientId) {
		Preconditions.checkEmptyString(clientId, "Invalid 'clientId' key");

		this.clientId = clientId;

		return this;
	}

	public InstagramSubscription clientSecret(String clientSecret) {
		Preconditions.checkEmptyString(clientSecret, "Invalid 'clientSecret' key");

		this.clientSecret = clientSecret;

		return this;
	}

	public InstagramSubscription verifyToken(String verifyToken) {
		Preconditions.checkEmptyString(verifyToken, "Invalid 'verifyToken' key");

		this.verifyToken = verifyToken;

		return this;
	}

	public InstagramSubscription aspect(String aspect) {
		Preconditions.checkEmptyString(aspect, "Invalid 'aspect' key");

		this.aspect = aspect;

		return this;
	}
	
	/**
	 * Create user subscription
	 * 
	 * @throws InstagramException
	 */
	public SubscriptionCreationResponse createUserSubscription() throws InstagramException {

		Preconditions.checkEmptyString(clientId, "You must provide a clientId key");
		Preconditions.checkEmptyString(clientSecret, "You must provide a clientSecret");
		Preconditions.checkEmptyString(verifyToken, "You must provide a verifyToken");

		OAuthRequest request = new OAuthRequest(Verbs.POST, Constants.SUBSCRIPTION_ENDPOINT);

		// Add the oauth parameter in the body
		request.addBodyParameter(Constants.CLIENT_ID, this.clientId);
		request.addBodyParameter(Constants.CLIENT_SECRET, this.clientSecret);
		request.addBodyParameter(Constants.SUBSCRIPTION_TYPE, SubscriptionType.USERS.toString());
		request.addBodyParameter(Constants.ASPECT, "media");
		request.addBodyParameter(Constants.VERIFY_TOKEN, this.verifyToken);
		request.addBodyParameter(Constants.CALLBACK_URL, callback);

        Response response;
        try {
            response = request.send();
        } catch (IOException e) {
            throw new InstagramException("Failed to create user subscription", e);
        }
        
        if (response.getCode() >= 200 && response.getCode() < 300) {
        	return createObjectFromResponse(SubscriptionCreationResponse.class, response.getBody());
        }
        
        throw handleInstagramError(response);
	}
	
	/**
	 * Create tag subscription
	 * 
	 * @param tagName
	 * @throws InstagramException
	 */
	public SubscriptionCreationResponse createTagSubscription(String tagName) throws InstagramException {

		Preconditions.checkEmptyString(clientId, "You must provide a clientId key");
		Preconditions.checkEmptyString(clientSecret, "You must provide a clientSecret");
		Preconditions.checkEmptyString(verifyToken, "You must provide a verifyToken");

		OAuthRequest request = new OAuthRequest(Verbs.POST, Constants.SUBSCRIPTION_ENDPOINT);

		// Add the oauth parameter in the body
		request.addBodyParameter(Constants.CLIENT_ID, this.clientId);
		request.addBodyParameter(Constants.CLIENT_SECRET, this.clientSecret);
		request.addBodyParameter(Constants.SUBSCRIPTION_TYPE, SubscriptionType.TAGS.toString());
		request.addBodyParameter(Constants.ASPECT, "media");
		request.addBodyParameter(Constants.VERIFY_TOKEN, this.verifyToken);
		request.addBodyParameter(Constants.CALLBACK_URL, callback);
		request.addBodyParameter(Constants.OBJECT_ID, tagName);

        Response response;
        try {
            response = request.send();
        } catch (IOException e) {
            throw new InstagramException("Failed to create user subscription", e);
        }
		
		if (response.getCode() >= 200 && response.getCode() < 300) {
        	return createObjectFromResponse(SubscriptionCreationResponse.class, response.getBody());
        }
        
        throw handleInstagramError(response);
	}
	
	public SubscriptionCreationResponse createLocationSubscription(String locationId) throws InstagramException {

		Preconditions.checkEmptyString(clientId, "You must provide a clientId key");
		Preconditions.checkEmptyString(clientSecret, "You must provide a clientSecret");
		Preconditions.checkEmptyString(verifyToken, "You must provide a verifyToken");

		OAuthRequest request = new OAuthRequest(Verbs.POST, Constants.SUBSCRIPTION_ENDPOINT);

		// Add the oauth parameter in the body
		request.addBodyParameter(Constants.CLIENT_ID, this.clientId);
		request.addBodyParameter(Constants.CLIENT_SECRET, this.clientSecret);
		request.addBodyParameter(Constants.SUBSCRIPTION_TYPE, SubscriptionType.LOCATIONS.toString());
		request.addBodyParameter(Constants.ASPECT, "media");
		request.addBodyParameter(Constants.VERIFY_TOKEN, this.verifyToken);
		request.addBodyParameter(Constants.CALLBACK_URL, callback);
		request.addBodyParameter(Constants.OBJECT_ID, locationId);

        Response response;
        try {
            response = request.send();
        } catch (IOException e) {
            throw new InstagramException("Failed to create user subscription", e);
        }

        if (response.getCode() >= 200 && response.getCode() < 300) {
        	return createObjectFromResponse(SubscriptionCreationResponse.class, response.getBody());
        }
        
        throw handleInstagramError(response);
	}

	/**
	 * Create geography subscription
	 * 
	 * @param latitude Latitude of the center search coordinate.
	 * @param longitude Longitude of the center search coordinate.
	 * @param radius Radius of the area you'd like to capture around that point (maximum radius is 5000 meters)
	 * @throws InstagramException
	 */
	public SubscriptionCreationResponse createGeographySubscription(double latitude, double longitude, int radius) throws InstagramException {

		Preconditions.checkEmptyString(clientId, "You must provide a clientId key");
		Preconditions.checkEmptyString(clientSecret, "You must provide a clientSecret");
		Preconditions.checkEmptyString(verifyToken, "You must provide a verifyToken");

		OAuthRequest request = new OAuthRequest(Verbs.POST, Constants.SUBSCRIPTION_ENDPOINT);

		// Add the oauth parameter in the body
		request.addBodyParameter(Constants.CLIENT_ID, this.clientId);
		request.addBodyParameter(Constants.CLIENT_SECRET, this.clientSecret);
		request.addBodyParameter(Constants.SUBSCRIPTION_TYPE, SubscriptionType.GEOGRAPHIES.toString());
		request.addBodyParameter(Constants.ASPECT, "media");
		request.addBodyParameter(Constants.VERIFY_TOKEN, this.verifyToken);
		request.addBodyParameter(Constants.CALLBACK_URL, callback);
		request.addBodyParameter(Constants.LATITUDE, Double.toString(latitude));
		request.addBodyParameter(Constants.LONGITUDE, Double.toString(longitude));
		request.addBodyParameter(Constants.RADIUS, Integer.toString(radius));

        Response response;
        try {
            response = request.send();
        } catch (IOException e) {
            throw new InstagramException("Failed to create geography subscription", e);
        }
		
		if (response.getCode() >= 200 && response.getCode() < 300) {
        	return createObjectFromResponse(SubscriptionCreationResponse.class, response.getBody());
        }
        
        throw handleInstagramError(response);
	}
	
	/**
	 * Get current subscriptions
	 * 
	 * @throws InstagramException
	 */
	public SubscriptionResponse getSubscriptionList() throws InstagramException {
		
		Preconditions.checkEmptyString(clientId, "You must provide a clientId key");
		Preconditions.checkEmptyString(clientSecret, "You must provide a clientSecret");
		
		OAuthRequest request = new OAuthRequest(Verbs.GET, Constants.SUBSCRIPTION_ENDPOINT);

		// Add the oauth parameter in the body
		request.addQuerystringParameter(Constants.CLIENT_ID, this.clientId);
		request.addQuerystringParameter(Constants.CLIENT_SECRET, this.clientSecret);

        Response response;
        try {
            response = request.send();
        } catch (IOException e) {
            throw new InstagramException("Failed to get subscription list", e);
        }
		
		if (response.getCode() >= 200 && response.getCode() < 300) {
        	return createObjectFromResponse(SubscriptionResponse.class, response.getBody());
        }
        
        throw handleInstagramError(response);
	}
	
	/**
	 * Delete subscriptions
	 * 
	 * @param subscriptionId
	 * @throws InstagramException
	 */
	public void deleteSubscription(long subscriptionId) throws InstagramException {
		
		Preconditions.checkEmptyString(clientId, "You must provide a clientId key");
		Preconditions.checkEmptyString(clientSecret, "You must provide a clientSecret");

		OAuthRequest request = new OAuthRequest(Verbs.DELETE, Constants.SUBSCRIPTION_ENDPOINT);

		// Add the oauth parameter in the body
		request.addQuerystringParameter(Constants.CLIENT_ID, this.clientId);
		request.addQuerystringParameter(Constants.CLIENT_SECRET, this.clientSecret);
		request.addQuerystringParameter("id", Long.toString(subscriptionId));
		
		Response response;
        try {
        	response = request.send();
        } catch (IOException e) {
            throw new InstagramException("Failed to delete all subscriptions", e);
        }
        
        if (response.getCode() >= 200 && response.getCode() < 300) {
        	return;
        }
        
        throw handleInstagramError(response);
	}
	
	/**
	 * Delete subscriptions
	 * 
	 * @param object Object type of subscription
	 * @throws InstagramException
	 */
	public void deleteSubscription(String object) throws InstagramException {
		
		Preconditions.checkEmptyString(clientId, "You must provide a clientId key");
		Preconditions.checkEmptyString(clientSecret, "You must provide a clientSecret");

		OAuthRequest request = new OAuthRequest(Verbs.DELETE, Constants.SUBSCRIPTION_ENDPOINT);

		// Add the oauth parameter in the body
		request.addQuerystringParameter(Constants.CLIENT_ID, this.clientId);
		request.addQuerystringParameter(Constants.CLIENT_SECRET, this.clientSecret);
		request.addQuerystringParameter("object", object);

		Response response;
        try {
        	response = request.send();
        } catch (IOException e) {
            throw new InstagramException("Failed to delete all subscriptions", e);
        }
        
        if (response.getCode() >= 200 && response.getCode() < 300) {
        	return;
        }
        
        throw handleInstagramError(response);
	}
	
	/**
	 * Get recent media from a geography subscription
	 *
	 * @param geoId id of the geography.
	 * @throws InstagramException if any error occurs.
	 */
	public GeographyMediaFeed getRecentMediaByGeography(String geoId) throws InstagramException {
		
		return getRecentMediaByGeography(geoId, null, 0);
	}
	
	/**
	 * Get recent media from a geography subscription
	 *
	 * @param geoId id of the geography.
	 * @param minId Return media before this min_id.
     * @param count Max number of media to return.
	 * @throws InstagramException if any error occurs.
	 */
	public GeographyMediaFeed getRecentMediaByGeography(String geoId, String minId, long count) throws InstagramException {
		
		Preconditions.checkEmptyString(clientId, "You must provide a clientId key");
		
		OAuthRequest request = new OAuthRequest(Verbs.GET, String.format(Constants.GEOGRAPHIES_ENDPOINT, geoId));

		// Add the oauth parameter in the body
		request.addQuerystringParameter(Constants.CLIENT_ID, this.clientId);
		
		if (minId != null) {
			request.addQuerystringParameter(QueryParam.MIN_ID,String.valueOf(minId));
        }

        if (count != 0) {
        	request.addQuerystringParameter(QueryParam.COUNT,String.valueOf(count));
        }

        Response response;
        try {
            response = request.send();
        } catch (IOException e) {
            throw new InstagramException("Failed to get subscription list", e);
        }
        
        if (response.getCode() >= 200 && response.getCode() < 300) {
        	return createObjectFromResponse(GeographyMediaFeed.class, response.getBody());
        }
        
        throw handleInstagramError(response);
	}
	
	/**
	 * Delete all subscriptions
	 * 
	 * @throws InstagramException
	 */
	public void deleteAllSubscription() throws InstagramException {
		
		deleteSubscription("all");
	}
	
	/**
	 * Creates an object from the JSON response and the class which the object would be mapped to.
	 *
	 * @param clazz a class instance
	 * @param response a JSON feed
	 * @return a object of type <T>
	 * @throws InstagramException if any error occurs.
	 */
    private <T> T createObjectFromResponse(Class<T> clazz, final String response) throws InstagramException {
        Gson gson = new Gson();
        T object = null;

        try {
            object = clazz.newInstance();
            object = gson.fromJson(response, clazz);
		}
		catch (InstantiationException e) {
			throw new InstagramException("Problem in Instantiation of type " + clazz.getName(), e);
		}
		catch (IllegalAccessException e) {
			throw new InstagramException("Couldn't create object of type " + clazz.getName(), e);
		}
		catch (Exception e) {
			throw new InstagramException("Error parsing json to object type " + clazz.getName(), e);
		}

        return object;
	}
        
    private InstagramException handleInstagramError(Response response) throws InstagramException {
        Gson gson = new Gson();
        final InstagramErrorResponse error;
        try {
            if (response.getCode() == 400) {
                error = gson.fromJson(response.getBody(), InstagramErrorResponse.class);
                error.throwException();
            }
            //sending too many requests too quickly;
            //limited to 5000 requests per hour per access_token or client_id overall.  (according to spec)
            else if (response.getCode() == 503) {
                error = gson.fromJson(response.getBody(), InstagramErrorResponse.class);
                error.throwException();
            }
        } catch (JsonSyntaxException e) {
            throw new InstagramException("Failed to decode error response " + response.getBody(), e);
        }
        throw new InstagramException("Unknown error response code: " + response.getCode() + " " + response.getBody());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("InstagramSubscription [");
        if (aspect != null)
            builder.append("aspect=").append(aspect).append(", ");
        if (callback != null)
            builder.append("callback=").append(callback).append(", ");
        if (clientId != null)
            builder.append("clientId=").append(clientId).append(", ");
        if (clientSecret != null)
            builder.append("clientSecret=").append(clientSecret).append(", ");
        if (verifyToken != null)
            builder.append("verifyToken=").append(verifyToken);
        builder.append("]");
        return builder.toString();
    }

}
