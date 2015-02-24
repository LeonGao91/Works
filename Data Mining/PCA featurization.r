#######################################################
#Homework 3
#Name:  Liang Gao
#andrew ID: liangg
#email: liangg@andrew.cmu.edu
#######################################################

#Problem 1

#####################################
#code to download yahoo finance data#
####################################
# source('yahoo_finance_data_loading.R')
# ticker.list <- read.csv('SP500_ticker.csv',header=TRUE)[,1]
# first.day <- "2011-01-01"
# last.day <-"2014-12-31"
# 
# 
# 
# download.data <- NULL
# for (ticker in ticker.list){
#     print(ticker)
#     dataset <- NULL
#     try(dataset<- data.loading(ticker, first.day, last.day)$close)
#     download.data <-cbind(download.data, dataset)
# }
# 
# date <- row.names(download.data)
# download.data <- data.frame(date=date,download.data)
# write.table(download.data,'SP500_close_price_raw.csv',quote=FALSE,sep=",",row.names=FALSE)
# 
# #remove stocks with more than na.thresh missing values
# na.thresh <- 60
# stay.col <- colnames(download.data)[which(colSums(1*(is.na(download.data)))<na.thresh)]
# download.data2 <- download.data[,stay.col]
# write.csv(download.data2, 'SP500_close_price.csv')

#reference for zoo package
#http://cran.r-project.org/web/packages/zoo/vignettes/zoo-quickref.pdf
library(zoo)
library(plyr)
library(leaps)
library(MASS)

import.csv <- function(filename){
  return(read.csv(filename,sep="," ,header=TRUE, strip.white = TRUE))
}

write.csv <- function(ob,filename){
  write.table(ob, filename, quote=FALSE, sep=",", row.names=FALSE)
}

##############################
#prepare data for PCA analysis
##############################
mydata1 <-import.csv('SP500_close_price.csv')
mydata <- subset(mydata1, select = -1)
date <- as.Date(as.character(mydata[, 1]))
myzoo <- zoo(mydata[,-1], date )
myzoo <- na.locf(myzoo) #impute missing values
prices2returns <- function(x) 100*diff(log(x)) #function to covert from price to return
log.return.zoo <- prices2returns(myzoo)
log.return.data <- coredata(log.return.zoo) #data
log.return.date <- time(log.return.zoo) #date

options(digits = 4, scipen = 999)

##############################
#problem 1
##############################

#a1
pca <- prcomp(log.return.zoo, scale=TRUE)
screeplot(pca)

#a2
var.propotion <- pca$sdev^2  / sum(pca$sdev^2)
var.cumulative <- cumsum(var.propotion)
plot(var.cumulative)

#a3
cat(which(var.cumulative >= 0.8)[1], "principal components must be retained in order to capture at least 80% of the total variance in data\n")

#a4
var <- pca$sdev^2
ans <- sum(var[-(1:2)]) / sum(var)
cat("The magnitude of the estimated reconstruction error if we only retain top two of the PCA components", ans, '\n')

#b1
df <- data.frame(date = log.return.date, value = pca$x[,1])
plot(df,type = "l")
min_date <- df[which.min(df[, 2]), 1]
print(min_date)
print("Black Monday 2011 refers to August 8, 2011, when US and global stock markets crashed")

#b2
weight = pca$rotation[, (1:2)]

#b3
ticker.lookup <- import.csv("SP500_ticker.csv")
b.df <- data.frame(ticker = rownames(weight), weight)
weight.df <- merge(b.df, ticker.lookup)
first <- aggregate(weight.df$PC1, by = list(sector = weight.df$sector), FUN = mean)
print(first)
barplot(first$x, names.arg = first$sector)
cat("1st principal component have captured most information about Financials")

#b4
second <- aggregate(weight.df$PC2, by = list(sector = weight.df$sector), FUN = mean)
print(second)
barplot(second$x, names.arg = second$sector)
cat("2nd principal component have captured most information about Utilities\n")

##############################
#problem2
##############################
BMI <- import.csv("BMI.csv")

#a
filter.features.by.cor <- function(df) {
  nf = ncol(df)
  names <- colnames(df)
  res <- data.frame(feature = character(0), correlations = numeric(0))
  for(i in 1:nf-1) {
    c <- cor(df[,i], df[,nf])
    res <- rbind(res, data.frame(feature = names[i], correlations = c))
  }
  return(res[order(-res$correlations), 1:2])
}

print(filter.features.by.cor(BMI))

#b
filter.features.by.exh <- function(df) {
  strfunc = paste(names(df)[ncol(df)],"~.",sep="")
  func = as.formula(strfunc)
  leaps <- regsubsets(func, data = df, nbest = 1,nvmax = 3)
  summary(leaps)
}

filter.features.by.exh(BMI)
#c

strfunc = paste(names(df)[ncol(df)],"~.",sep="")
func = as.formula(strfunc)
fit <- lm(func, data = BMI)
step <- stepAIC(fit, direction="backward")
step$anova

