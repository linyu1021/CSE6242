package edu.gatech.cse6242;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.io.IOException;
import java.util.StringTokenizer;
import java.lang.Object;


//import javax.xml.soap.Text;

public class Q4 {
  
  public static class TokenizerMapper
       extends Mapper<Object, Text, IntWritable, IntWritable>{

    private IntWritable vertex = new IntWritable();
    private final IntWritable outgoing = new IntWritable(1);
    private final IntWritable incoming = new IntWritable(-1);
    public void map(Object key, Text value, 
                    Context context
                    ) throws IOException, InterruptedException{
      StringTokenizer itr = new StringTokenizer(value.toString(), "\n");
      while(itr.hasMoreTokens()) {
        String line = itr.nextToken();
        String array[] = line.split("\t");
        vertex.set(Integer.parseInt(array[0]));
        context.write(vertex, outgoing);
        vertex.set(Integer.parseInt(array[1]));
        context.write(vertex, incoming);
      }
    }
  }
  
  public static class IntSumReducer
       extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable>{

    private IntWritable difference = new IntWritable();
    public void reduce(IntWritable key, Iterable<IntWritable> values, 
                       Context context
                       ) throws IOException, InterruptedException{
      int sum = 0;
      for(IntWritable val: values) {
        sum += val.get();
      } 
      difference.set(sum);
      context.write(key, difference);
    }
  }
  
  public static class TokenizerMapper1 
       extends Mapper<Object, Text, IntWritable, IntWritable>{

    private final static IntWritable one = new IntWritable(1);
    private IntWritable difference1 = new IntWritable();
    public void map(Object key1, Text values1, 
                    Context context
                    ) throws IOException, InterruptedException{
      StringTokenizer itr1 = new StringTokenizer(values1.toString(), "\n");
      while(itr1.hasMoreTokens()) {
        String line1 = itr1.nextToken();
        String tokens1[] = line1.split("\t");
        difference1.set(Integer.parseInt(tokens1[1]));
        context.write(difference1, one);
      }
    }
  }
  
  public static class IntSumReducer1
       extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable>{

    private IntWritable count = new IntWritable();
    public void reduce(IntWritable key1, Iterable<IntWritable> values1, 
                       Context context
                       ) throws IOException, InterruptedException{
      int sum1 = 0;
      for(IntWritable val1: values1) {
        sum1 += val1.get();
      } 
      count.set(sum1);
      context.write(key1, count);
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "Q4");

    job.setJarByClass(Q4.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(IntWritable.class);
    job.setOutputValueClass(IntWritable.class);


    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path("output"));
    job.waitForCompletion(true);

    Configuration conf1 = new Configuration();
    Job job1 = Job.getInstance(conf1, "Q4");

    job2.setJarByClass(Q4.class);
    job2.setMapperClass(TokenizerMapper1.class);
    job2.setCombinerClass(IntSumReducer1.class);
    job2.setReducerClass(IntSumReducer1.class);
    job2.setOutputKeyClass(IntWritable.class);
    job2.setOutputValueClass(IntWritable.class);


    FileInputFormat.addInputPath(job1, new Path("output"));
    FileOutputFormat.setOutputPath(job1, new Path(args[1]));
    System.exit(job1.waitForCompletion(true) ? 0 : 1);
  }
}



