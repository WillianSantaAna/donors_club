package pt.donors.club.donors_club.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import pt.donors.club.donors_club.models.AdPost;
import pt.donors.club.donors_club.models.views.AdPostSimpleView;
import pt.donors.club.donors_club.models.views.AdPostView;

public interface AdPostRepository extends CrudRepository<AdPost, Integer> {
  String queryAdPostSimpleView = "SELECT ad.ad_id as id, ad.ad_title as title, ad.ad_pub_date as pubDate, c.city_name as city "
                            + "FROM adposts ad "
                            + "INNER JOIN users u ON ad.ad_owner_id = u.usr_id "
                            + "INNER JOIN cities c ON u.usr_city_id = c.city_id";

  String querySearchAdPosts = "SELECT ad.ad_id as id, ad.ad_title as title, ad.ad_pub_date as pubDate, c.city_name as city "
                            + "FROM adposts ad "
                            + "INNER JOIN users u on ad.ad_owner_id = u.usr_id "
                            + "INNER JOIN cities c on u.usr_city_id = c.city_id "
                            + "INNER JOIN subcategories s on ad.ad_subcategory_id = s.subc_id "
                            + "INNER JOIN categories cat on s.subc_cat_id = cat.cat_id "
                            + "WHERE ad.ad_title LIKE %:title% AND c.city_name LIKE :city% "
                            + "AND (s.subc_name LIKE :category% OR cat.cat_name LIKE :category%)";

  @Query(value = queryAdPostSimpleView, nativeQuery = true)
  Iterable<AdPostSimpleView> findAllSimpleView();

  @Query(value = "SELECT ad_title as title, ad_description as description, ad_owner_id as owner FROM adposts WHERE ad_id = :id", nativeQuery = true)
  Optional<AdPostView> findAdPostById(@Param("id") int id);

  @Query(value = queryAdPostSimpleView + " WHERE ad.ad_owner_id = :id", nativeQuery = true)
  Iterable<AdPostSimpleView> findAllMyAdPostsSimpleView(@Param("id") int id);

  @Query(value = queryAdPostSimpleView
      + " INNER JOIN wishlist w ON ad.ad_id = w.wl_ad_id WHERE w.wl_usr_id = :id", nativeQuery = true)
  Iterable<AdPostSimpleView> findWishListSimpleView(@Param("id") int id);

  @Query(value = querySearchAdPosts, nativeQuery = true)
  Iterable<AdPostSimpleView> findByTitleAndByCategoryAndByCityContaining(@Param("title") String title,
      @Param("category") String category, @Param("city") String city);
}
