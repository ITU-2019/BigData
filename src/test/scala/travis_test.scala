import org.scalatest._
import collection.mutable.Stack
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.Row
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.Encoders
import org.apache.spark.sql.types._



class Travis extends FunSpec with Matchers {
    describe("A Set") {
        describe("when empty") {
            it("should have size 0") {
                assert(Set.empty.size == 0)
            }

            it("should produce NoSuchElementException when head is invoked") {
                assertThrows[NoSuchElementException] {
                    Set.empty.head
                }
            }
        }
    }

    describe("Spark") {
        it("Init Spark") {
            val spark = SparkSession.builder.
    					appName("MyApp").
    					master("local").
    					getOrCreate
            spark should not be null
        }
        it("Load datafile Spark") {
            val p = new PegiRatings()
            val pegiDF = p.pegiDFLoader("data/pegi_ratings.csv")

    		println("No. of 18+ games: " + p.pegiCount18(pegiDF))
    		println("No. of PS2-PS4 games with 18+ rating: " + p.pegi18forPS2toPS4(pegiDF)._2)
    		println("Newest 18+ game: " + p.pegi18Newest(pegiDF).first)
    		println("Since 2010 the top 3 genres have been: " + p.highestGenreCountSince2010(pegiDF))

            p.pegiCount18(pegiDF) should be 999


        }
    }
}
