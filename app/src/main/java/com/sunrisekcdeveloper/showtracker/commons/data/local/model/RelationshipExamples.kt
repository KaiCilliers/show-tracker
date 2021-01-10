/*
 * Copyright © 2020. The Android Open Source Project
 *
 * @author Kai Cilliers
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sunrisekcdeveloper.showtracker.commons.data.local.model

import androidx.room.*

/**
 * Relationship examples
 *
 * Note: If the @Relation annotation does not meet your specific
 * use case, you might need to use the JOIN keyword in your SQL
 * queries to manually define the appropriate relationships.
 *
 * Conclusion is to create the Entity classes that you want
 * modeled as tables. There is no need to enforce relationships
 * between these classes, that will be handled when you create
 * data classes to fetch the data from the table in a format
 * that you desire. Think in objects and not in table entities.
 *
 * @constructor Create empty Relationship examples
 */
object RelationshipExamples {

    /**
     * TODO
     *  One to One Relationship
     *  1)  Create two entities
     *  2)  One entity must include a variable that is a reference to the
     *      primary key of the other entity
     *  3)  Create a new data class where each instance will hold an
     *      instance of the parent entity and the corresponding
     *      instance of the child entity
     *  4)  Add the @Embedded annotation to the instance of the parent
     *      entity
     *  5)  Add the @Relation annotation to the instance of the child
     *      entity, with parentColumn set to the name of the PK column
     *      of the parent entity and entityColumn set to the name of
     *      the column of the child entity that references the parent
     *      entity's PK
     */
    @Entity
    data class UserR(
        @PrimaryKey val userId: Long,
        val name: String,
        val age: Int
    )
    @Entity
    data class LibraryR(
        @PrimaryKey val libraryId: Long,
        val userOwnerId: Long
    )
    data class UserAndLibraryR(
        @Embedded val user: UserR,
        @Relation(
            parentColumn = "userId",
            entityColumn = "userOwnerId"
        )
        val library: LibraryR
    )
    // This is how you'd query the results
//    @Transaction
//    @Query("SELECT * FROM UserR")
//    fun getUsersAndLibraries(): List<UserAndLibraryR>

    /**
     * TODO
     *  One-To-Many Relationship
     *  1)  Create a class for each entity. Again, ensure the child
     *      entity includes a variable that is a reference to the
     *      PK of the parent entity
     *  2)  Create a new data class to model the relationship between
     *      the two entities. Each instance hold an instance of the
     *      parent entity and a list of all corresponding child entity
     *      instances
     *  4)  Add the @Embedded annotation to the parent instance
     *  3)  Add the @Relation annotation to the instance of the child
     *      entity, with parentColumn set to the name of the primary
     *      key column of the parent entity and entityColumn set to
     *      the name of the column of the child entity that
     *      references the parent entity's primary key
     */
    @Entity
    data class UserM(
        @PrimaryKey val userId: Long,
        val name: String,
        val age: Int
    )
    @Entity
    data class Playlists(
        @PrimaryKey val playlistId: Long,
        val userCreatorId: Long,
        val playlistName: String
    )
    data class UserWithPlaylists(
        @Embedded val user: UserM,
        @Relation(
            parentColumn = "userId",
            entityColumn = "userCreatorId"
        )
        val playlists: List<Playlists>
    )
    // This is how you'd query the results
//    @Transaction
//    @Query("SELECT * FROM UserM")
//    fun getUsersWithPlaylists(): List<UserWithPlaylists>

    /**
     * TODO
     *  Many-To-Many Relationship
     *  1)  Create a class for each entity. Note, you are not
     *      including a reference to the parent entity in the
     *      child entity
     *  2)  Create a third entity to represent an associative
     *      entity (or cross-reference table) between the two
     *      entities. Include a single variable for each
     *      instance reference of the two entities
     *  3)  Create a composite primary key using the two
     *      variables referencing the two entities
     *  4)  IF you want to query playlists and a list of the
     *      corresponding songs for each playlist, create a
     *      new data class that contains a single Playlist
     *      object and a list of all the Song objects that
     *      the playlist includes. This class follows the
     *      same standard of adding @Embedded and @Relation,
     *      except by including an additional associateBy
     *      value in @Relation. Set it to the reference
     *      entity
     *  5)  IF you want to query songs and a list of the
     *      corresponding playlists for each, create a new
     *      data class that contains a single Song object
     *      and a list of all the Playlist objects in which
     *      the song is included. This class follows the
     *      same standard of adding @Embedded and @Relation,
     *      except by including an additional associateBy
     *      value in @Relation. Set it to the reference
     *      entity
     */
    @Entity
    data class PlaylistMM(
        @PrimaryKey val playlistId: Long,
        val playlistName: String
    )
    @Entity
    data class Song(
        @PrimaryKey val songId: Long,
        val songName: String,
        val artist: String
    )
    @Entity(primaryKeys = ["playlistId", "songId"])
    data class PlaylistSongCrossRef(
        val playlistId: Long,
        val songId: Long
    )
    // 4)
    data class PlaylistWithSongs(
        @Embedded val playlist: PlaylistMM,
        @Relation(
            parentColumn = "playlistId",
            entityColumn = "songId",
            associateBy = Junction(PlaylistSongCrossRef::class)
        )
        val songs: List<Song>
    )
    // 5)
    data class SongWithPlayLists(
        @Embedded val song: Song,
        @Relation(
            parentColumn = "songId",
            entityColumn = "playlistId",
            associateBy = Junction(PlaylistSongCrossRef::class)
        )
        val playlists: List<PlaylistMM>
    )
    // This is how you'd query the results
    // 4)
//    @Transaction
//    @Query("SELECT * FROM Playlist")
//    fun getPlaylistsWithSongs(): List<PlaylistWithSongs>

    // 5)
//    @Transaction
//    @Query("SELECT * FROM Song")
//    fun getSongsWithPlaylists(): List<SongWithPlaylists>

    /**
     * TODO
     *  Defining nested relationships
     *  Scenario: You want to query all the users with their
     *      playlists and the songs in each playlist
     *  1)  Carry over some of the above classes. To recap,
     *      users have a 1 : many relationship with playlists,
     *      and playlists have a many : many relationship with
     *      songs.
     *  2)  Create your data class that represents an instance
     *      of a playlist a list of associated song instances,
     *      just like in the previous example
     *  3)  Create another data class that models the relationship
     *      between another table (our user entity) and the first
     *      relationship data class, thus "nesting" the relationship
     *      within the new one. You will create a one to many
     *      relationship between the user entity and the
     *      PlaylistWithSongs relationship (data) class. Be sure
     *      to add the required entity value in the @Relation
     *      annotation to the "parent" of the relationship
     *      class (playlist in this case)
     *  Note: The UserWIthPlaylistsAndSongs class directly
     *      models the relationships between all three of
     *      the entity classes: User, Playlist, and Song.
     *  Visual of Hierarchy:
     *  .
     *  .
     *              UserWithPlaylistsAndSongs
     *                          PlaylistWithSongs
     *      User            Playlist            Song
     *  .
     *  .
     *  Visual of hierarchy taken further (un-tested):
     *  .
     *  .
     *              HouseOfUsersWithPlaylistsAndSong
     *                              UsersWithPlaylistsAndSongs
     *                                          PlaylistWithSongs
     *      House           User            Playlist            Song
     */
    @Entity
    data class UserN(
        @PrimaryKey val userId: Long,
        val name: String,
        val age: Int
    )
    @Entity
    data class PlaylistN(
        @PrimaryKey val playlistId: Long,
        val userCreatorId: Long,
        val playlistName: String
    )
    @Entity
    data class SongN(
        @PrimaryKey val songId: Long,
        val songName: String,
        val artist: String
    )
    @Entity(primaryKeys = ["playlistId", "songId"])
    data class PlaylistSongCrossReffN(
        val playlistId: Long,
        val songId: Long
    )
    data class PlaylistWithSongsN(
        @Embedded val playlist: PlaylistN,
        @Relation(
            parentColumn = "playlistId",
            entityColumn = "songId",
            associateBy = Junction(PlaylistSongCrossReffN::class)
        )
        val song: List<SongN>
    )
    data class UserWithPlaylistsAndSongs(
        @Embedded val user: UserN,
        @Relation(
            entity = PlaylistN::class,
            parentColumn = "userId",
            entityColumn = "userCreatorId"
        )
        val playlists: List<PlaylistWithSongsN>
    )
    // This is how you'd query the results
//    @Transaction
//    @Query("SELECT * FROM UserN")
//    fun getUsersWithPlaylistsAndSongs(): List<UserWithPlaylistsAndSongs>

    /**
     * TODO
     *  Advanced use case: Returning a different object which is not
     *  an entity but contains some of the fields
     *  1)  Simply specify the entity in the @Relation annotation and
     *      return your different object
     */
    @Entity
    data class DogA(
        @PrimaryKey val dogId: Long,
        val dogOwnerId: Long,
        val name: String,
        val cuteness: Int,
        val barkVolume: Int,
        val breed: String
    )
    @Entity
    data class OwnerA(
        @PrimaryKey val ownerId: Long,
        val name: String
    )
    // Normal case of returning a list of owners and their dogs
    data class OwnerWithDogs(
        @Embedded val owner: OwnerA,
        @Relation(
            parentColumn = "ownerId",
            entityColumn = "dogOwnerId"
        )
        val dogs: List<DogA>
    )
    // Advanced use case
    data class PupA(
        val name: String,
        val cuteness: Int = 11
    )
    data class OwnerWithPups(
        @Embedded val owner: OwnerA,
        @Relation(
            parentColumn = "ownerId",
            entity = DogA::class,
            entityColumn = "dogOwnerId"
        )
        val dogs: List<PupA>
    )

    /**
     * TODO
     *  Advanced use case: Returning specific columns from an entity
     *  1)  Simply specify the required fields in the projection
     *      parameter in @Relation
     *  2)  Remember to also specify the entity parameter any time
     *      you don't return the entity itself
     */
    data class OwnerWithDogsB(
        @Embedded val owner: OwnerA,
        @Relation(
            parentColumn = "ownerId",
            entity = DogA::class,
            entityColumn = "dogOwnerId",
            projection = ["name"]
        )
        val dogNames: List<String>
    )
}