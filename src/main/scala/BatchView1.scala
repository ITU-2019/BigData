
// Get a dataset that describe the connection strength for each router at each day.

// Case class for the output table structure.
// Did | location | deviceFunction | deviceMode | uniformRoom | avg(rssi) | avg(snRatio) | day | ts
case class View1(did:String, 
		location:String, 
		deviceFunction:String, 
		deviceMode:String,
		deviceName:String,
		uniformRoom:String,
		avgRssi:Double,
		avgSnRatio:Double,
		date:String)

object BatchView1 {
	
	var view:Dataset[View1] = Seq.empty[View1].toDS

	def construct():Unit = {
		println("Constructing batch view 1...")

		val toDateTime = udf((ts: Long) => {
			val df = new SimpleDateFormat("yyyy-MM-dd")
			val date = df.format(ts * 1000L)
			date
			
		})

		view = BatchLayer.masterDataset.devices
			.withColumn("date", toDateTime($"ts"))
			.groupBy("did", "date")
			.agg(avg("rssi"), avg("snRatio"))
			.join(BatchLayer.masterDataset.routers, "did")
			.drop("upTime")
			.withColumnRenamed("avg(rssi)", "avgRssi")
			.withColumnRenamed("avg(snRatio)", "avgSnRatio")
			.as[View1]
		
		println("Done constructing batch view 1")
	}
}