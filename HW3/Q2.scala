package edu.gatech.cse6242

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.functions._

object Q2 {
    case class Line(source: String, target: String, weight: Int)
	def main(args: Array[String]) {
    	val sc = new SparkContext(new SparkConf().setAppName("Q2"))
		val sqlContext = new SQLContext(sc)
		import sqlContext.implicits._

    	// read the file
    	val file = sc.textFile("hdfs://localhost:8020" + args(0))
		/* TODO: Needs to be implemented */

        val total = file.map(p => Line(p.split("\t")(0), p.split("\t")(1), p.split("\t")(2).trim.toInt)).toDF()
        
        val dfsrc = total.filter($"weight" >= 5).groupBy($"source").agg(sum("weight")as("outgoing"))
        
        val dftgt = total.filter($"weight" >= 5).groupBy($"target").agg(sum("weight")as("incoming"))

        var result = dfsrc.join(dftgt, $"source" === $"target", "out")
                          .withColumn("diff", when(col("incoming").isNull, lit(0))
                          .otherwise(col("incoming")) - when(col("outgoing").isNull, lit(0)).otherwise(col("outgoing")))
                          .withColumn("id", when(col("source").isNull, col("target")).otherwise(col("source")))
                          .select("id", "diff")
                          .map(p => p(0).toString() + "\t" + p(1).toString())

        //separate the data into rows
    //val filefilter = file.filter(s=>s.split("\t")(2) >="5")
    //val outgoing = filefilter.map(s=>(s.split("\t")(0),s.split("\t")(2).toInt*(-1)))
    //val incoming = filefilter.map(s=>(s.split("\t")(1),s.split("\t")(2).toInt))
    //val distance = outgoing union incoming

    //select elements from each row, then map and reduce
    //val result = distance.reduceByKey(_+_).map(x => (x._1.toString()+"\t"+x._2.toString()))
    	// store output on given HDFS path.
    	// YOU NEED TO CHANGE THIS
    	result.saveAsTextFile("hdfs://localhost:8020" + args(1))
  	}
}
