# Code for Problem - 1

# utility function for import from csv file
import.csv <- function(filename) {
  return(read.csv(filename, sep = ",", header = TRUE))
}

brief <- function(data) {
  cat(paste0("This dataset has ", nrow(data), " Rows " ,ncol(data), " Attributes\n\n"))
  cat("real valued attrbutes\n")
  cat("---------------------\n")
  symbolic_data = data.frame(ID = c(1:nrow(data)))
  dropped_columns <- c()
  real_value_columns <- c()
  for(i in 1:ncol(data)) {
    if(class(data[,i]) != "numeric" && class(data[,i]) != "integer") {
      symbolic_data[colnames(data)[i]] <- data[,i]
      dropped_columns <- c(dropped_columns, i)
    }
    else real_value_columns <- c(real_value_columns, i)
  }
  
  data <- subset(data, select = -dropped_columns)
  symbolic_data <- subset(symbolic_data, select = -1)
  
  countMissing <- function(v) {
    return( length(which(is.na(v))) + length(which(v == "")) )
  }
  
  getArity <- function(v) {
    v = levels(factor(v))
    v = v[which(v!="")]
    return( length(v) )
  }
  
  MCVs_counts <- function(v) {
    freq = as.data.frame(table(v))
    freq = freq[freq$v != "",]
    freq = freq[order(freq$Freq, decreasing = TRUE),]
    s = paste0()
    for(i in 1:nrow(freq)) {
      if(i <= 3) {
        s = paste0(s, freq[i, 1], '(', freq[i, 2], ') ')
      }
    }
    return( s )
  }
  
  data_result <- data.frame(Attribute_ID = real_value_columns,
                       Attribute_Name = colnames(data),
                       Missing = sapply(data, countMissing),
                       Mean = colMeans(data, na.rm = T),
                       Median = sapply(data, median, na.rm = T),
                       Sdev = sapply(data, sd, na.rm = T),
                       Min = sapply(data, min, na.rm = T),
                       Max = sapply(data, max, na.rm = T)
    )
  print(data_result)
  
  symbolic_result <- data.frame(Attribute_ID = dropped_columns,
                                Attribute_Name = colnames(symbolic_data),
                                Missing = sapply(symbolic_data, countMissing),
                                Arity = sapply(symbolic_data, getArity),
                                MCVs_counts = sapply(symbolic_data, MCVs_counts)
    )
  
  cat("\nsymbolic attrbutes\n")
  cat("---------------------\n")
  options(digits = 2, scipen = 999)
  print(symbolic_result)
}

cat("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n")
cat("brief function output for house_no_missing.csv\n")
cat("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n")
brief(import.csv("house_no_missing.csv"))
cat("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n")
cat("brief function output for house_with_missing.csv\n")
cat("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n")
brief(import.csv("house_with_missing.csv"))

house.data <- import.csv("house_no_missing.csv")

house.value = house.data[,1]
hist(house.value)

house_value_with_high_crime_rate = subset(house.data, house.data$Crime_Rate > 0.26)[,1]
house_value_with_lower_crime_rate = subset(house.data, house.data$Crime_Rate <= 0.26)[,1]
hist(house_value_with_high_crime_rate)
hist(house_value_with_lower_crime_rate)
plot(house.data$num_of_rooms, house.data$house_value)


#Code for problem 2


# You need to install package 'FNN'
library(FNN)
library(gplots)

options(digits = 2, scipen = 999)

# utility function for import from csv file
import.csv <- function(filename) {
    return(read.csv(filename, sep = ",", header = TRUE))
}

# utility function for export to csv file
write.csv <- function(ob, filename) {
    write.table(ob, filename, quote = FALSE, sep = ",", row.names = FALSE)
}

# Connect-the-dots model that learns from train set and is being tested using test set
# Assumes the last column of data is the output dimension
get_pred_dots <- function(train,test){
  nf <- ncol(train)
  input <- train[,-nf]
  query <- test[,-nf]
  my.knn <- get.knnx(input,query,k=2) # Get two nearest neighbors
  nn.index <- my.knn$nn.index
  pred <- rep(NA,nrow(test))
  for (ii in 1:nrow(test)){
    y1 <- train[nn.index[ii,1],nf]
    y2 <- train[nn.index[ii,2],nf]
    pred[ii] = (y1+y2)/2
  }
  return(pred)  
}

# Linear model
# Assumes the last column of data is the output dimension
get_pred_lr <- function(train,test){
  strfunc = paste(names(train)[ncol(train)],"~.",sep="") 
  func = as.formula(strfunc)
  my.model <- glm(func, data = train)
  pred <- predict(my.model, test[-ncol(test)])
  return(pred)
}

# Default predictor model
# Assumes the last column of data is the output dimension
get_pred_default <- function(train,test){
  res <- rep(mean(train[,ncol(train)]), nrow(test))
  return( res )
}

mse <- function(a, b) {
  result <- 0
  for(i in 1:length(a)) {
    result <- result + (a[i] - b[i]) ^ 2
  }
  return( result / length(a) )
}

do_cv <- function(df, output, k, model) {
  col_idx = grep(output, names(df))
  df = df[c((1:ncol(df))[-col_idx], col_idx)]
  cur <- 1
  x <- nrow(df) / k - 1
  res <- c()
  for(i in 1:k) {
    test <- df[cur:(cur + x),]
    train <- df[-(cur:(cur + x)),]
    res <- c(res, mse(model(train, test), test[,ncol(test)]))
    cur <- cur + x + 1
  }
  return( res )
}

getCI <- function(score) {
  e <- qnorm(0.95)*sd(score)/sqrt(length(score))
  m <- mean(score)
  return( c(m - e,  m + e) )
}

df <- import.csv("house_no_missing.csv")
house_value <- df[,"house_value"]
Crime_Rate <- sapply(df[,"Crime_Rate"], log)
newdf <- data.frame(house_value, Crime_Rate)

mse.pred_dot = do_cv(newdf, "house_value", nrow(df), get_pred_dots)
mse.pred_lr = do_cv(newdf, "house_value", nrow(df), get_pred_lr)
mse.pred_default = do_cv(newdf, "house_value", nrow(df), get_pred_default)

h = cbind(mse.pred_dot, mse.pred_lr, mse.pred_default)
barplot2(h)

print(getCI(do_cv(newdf, "house_value", 506, get_pred_dots)))
print(getCI(do_cv(newdf, "house_value", 506, get_pred_lr)))
print(getCI(do_cv(newdf, "house_value", 506, get_pred_default)))
