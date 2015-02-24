library("e1071")
library("class")

options(digits = 2, scipen = 999)

# utility function for import from csv file
import.csv <- function(filename) {
    return(read.csv(filename, sep = ",", header = TRUE))
}
#1.
#logistic regression classfier
get_pred_logreg <- function(train,test) {
    strfunc = paste(names(train)[ncol(train)],"~.",sep="")
    func = as.formula(strfunc)
    my.model <- glm(func, data=train, family=binomial)
    pred <- predict(my.model, test[, -ncol(test)], type='response')
    return(data.frame(pred, true = test[,ncol(test)]))
}

#svm classifier
get_pred_svm <- function(train,test) {
  strfunc = paste(names(train)[ncol(train)],"~.",sep="")
  func = as.formula(strfunc)
  my.model <- svm(func, data = train, probability=TRUE)
  pred <- attr(predict(my.model, test[, -ncol(test)], probability = TRUE), 'probabilities')[, '1'] 
  return(data.frame(pred, true = test[,ncol(test)]))
}

#naive bayes classifier
get_pred_nb <- function(train,test) {
  strfunc = paste(names(train)[ncol(train)],"~.",sep="")
  func = as.formula(strfunc)
  my.model <- naiveBayes(func, data = train)
  pred <- predict(my.model, test[-ncol(test)], type='raw')[, '1']
  return(data.frame(pred, true = test[,ncol(test)]))
}

#knn classifier
get_pred_knn <- function(train, test, knn) {
  nc <- ncol(train)
  my.model <- knn(train[, -nc], test[, -nc], train[,nc], k = knn, prob=TRUE)
  prob <- attr(my.model,"prob")
  pred.bi <- my.model
  pred <- ifelse(pred.bi=='1', prob, 1-prob)
  return(data.frame(pred, true = test[,ncol(test)]))
}

#default classifier
get_pred_default <- function(train, test) {
  nf <- ncol(train)
  num <- as.numeric(train[, nf])
  my.mean <- mean(num) - 1
  pred <- rep(my.mean, nrow(test))
  return (data.frame(pred, test[, nf]))
}

#2. do_cv(), which is similar to assignment 1
do_cv <- function(df, k, model) {
  #shuffle data
    df <- df[sample(nrow(df)),]
    cur <- 1
    x <- nrow(df) / k - 1
    res <- c()
    for(i in 1:k) {
        test <- df[cur:(cur + x),]
        train <- df[-(cur:(cur + x)),]
        #see if the model is "knn"
        if(class(model) == "character" && grep('nn', model)) {
          res <- rbind(res, get_pred_knn(train, test, as.integer(substr(model, 0, nchar(model) - 2))))
        }
        else {
          res <- rbind(res, model(train, test))
        }
        cur <- cur + x + 1
    }
    return( res )
}

#3. Get metrics which helps to find out required metrics of the test result.
get_metrics <- function(df, cutoff = 0.5) {
    true <- df[,2]
    test <- df[,1]
    test[which(test >= cutoff)] = 1
    test[which(test < cutoff)] = 0
    tp <- sum(true[which(test == 1)] == 1)
    fp <- sum(true[which(test == 1)] == 0)
    tpr <- tp / sum(true == 1)
    fpr <- fp / sum(true == 0)
    acc <- sum(test == true) / nrow(df)
    precision <- tp / (tp + fp)
    recall <- tpr
    return( data.frame(tpr, fpr, acc, precision, recall) )
}

#4(a)
df <- import.csv("wine.csv")
levels(df$type) <- c(0, 1) #high -> 0, low -> 1

#find the k in knn which generate the best result, and find out overfitting and underfitting according to that
finding_k_threshold <- function(df, metric){
  res <- c()
  for(i in 1:20) {
    knn <- paste(as.character(i),'nn')
    res <- rbind(res, get_metrics(do_cv(df, 10, knn)))
  }
  res <- res[, metric]
  plot(res)
  max <- which.max(res)
  cat("4(a) According to", metric, ", The underfitting range is from 1 to", max)
  cat(".\n     The overfitting range is >", max, ".\n")
}

#use acc as the metric to compare
finding_k_threshold(df, "acc")

#4(b)
#Compare models by different metrics
pred_logreg = do_cv(df, 10, get_pred_logreg)
pred_svm = do_cv(df, 10, get_pred_svm)
pred_nb = do_cv(df, 10, get_pred_nb)
pred_default = do_cv(df, 10, get_pred_default)
#choose 5nn which give good prediction according to (a)
pred_knn = do_cv(df, 10, "5nn")

metric_logreg <- get_metrics(pred_logreg)
metric_svm <- get_metrics(pred_svm)
metric_nb <- get_metrics(pred_nb)
metric_default <- get_metrics(pred_default)

metric_res <- rbind(metric_logreg, metric_svm, metric_nb, metric_default)
print(metric_res)

#Compare models by ROC
#ROC function which get fpr and tpr
ROC <- function(df){
  s <- df[, 1]
  label <- df[, 2]
  tpr <- sapply(s,function(v) sum(label[which(s>=v)]==1)/sum(label==1)) #predicting 1, true label is 1
  fpr <- sapply(s,function(v) sum(label[which(s>=v)]==0)/sum(label==0)) #predicting 1, true label is 0 
  tpr <- c(0,tpr,1)
  fpr <- c(0,fpr,1)
  out <- data.frame(tpr=tpr,fpr=fpr)
  out <- out[order(out$fpr,out$tpr),]
  return(out)
}

logreg <- ROC(pred_logreg)
svm <- ROC(pred_svm)
nb <- ROC(pred_nb)
knn <- ROC(pred_knn)
default <- ROC(pred_default)

#plot each model in the same chart to compare
plot(logreg$fpr,logreg$tpr,type='l',ylim=c(0,1),xlim=c(0,1),xlab="fpr", ylab="tpr")
lines(svm$fpr,svm$tpr, col='yellow', lwd=2.5)
lines(nb$fpr,nb$tpr, col='red', lwd=2.5)
lines(knn$fpr,knn$tpr, col='green', lwd=2.5)
lines(default$fpr,default$tpr, col='blue', lwd=2.5)
lines(seq(0,1,length.out=100),seq(0,1,length.out=100),lty=2)
legend(0.75,0.5,
       c("logreg","svm","nb","knn","default"), 
       lty=c(1,1,1,1,1), 
       lwd=c(2.5,2.5,2.5,2.5,2.5),
       col=c("black","yellow","red","green","blue"),
       cex=0.8
)
