//
//  PullToRefresh.h
//  Yilebang
//
//  Created by KimOC on 12/20/13.
//  Copyright (c) 2013 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol PullToRefreshDelegate;

@interface PullToRefreshTable : UITableView
{
    UIView *refreshFooterView;
    UILabel *refreshLabel;
    UIImageView *refreshArrow;
    UIActivityIndicatorView *refreshSpinner;
    BOOL isDragging;
    BOOL isLoading;
    NSString *textPull;
    NSString *textRelease;
    NSString *textLoading;
}

@property (nonatomic, strong) UIView *refreshFooterView;
@property (nonatomic, strong) UILabel *refreshLabel;
@property (nonatomic, strong) UIImageView *refreshArrow;
@property (nonatomic, strong) UIActivityIndicatorView *refreshSpinner;
@property (nonatomic, strong) NSString *textPull;
@property (nonatomic, strong) NSString *textRelease;
@property (nonatomic, strong) NSString *textLoading;
@property (nonatomic, strong) NSString *textNoMore;
@property (nonatomic) BOOL hasMore;


@property(strong, nonatomic) id <PullToRefreshDelegate> pulldelegate;

- (void)setupStrings;
- (void)addPullToRefreshFooter;
- (void)startLoading;
- (void)stopLoading;
//- (void)refresh;

- (void) BeginDragging;
- (void) DidScroll:(UIScrollView *)scrollView;
- (void) DidEndDragging:(UIScrollView *)scrollView;

@end

// pull to refresh protocol
@protocol PullToRefreshDelegate <NSObject>
- (void) pullRefresh:(UIScrollView *)scrollView;

@end
