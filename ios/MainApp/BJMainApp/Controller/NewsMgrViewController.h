//
//  NewsMgrViewController.h
//  BJMainApp
//
//  Created by KimOC on 7/23/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "NewsTableViewCell.h"

@interface NewsMgrViewController : UIViewController<UITableViewDataSource, UITableViewDelegate, UIScrollViewDelegate, MainSvcDelegate, NewsCellDelegate>
{
	/********************** UI Controls **********************/
	IBOutlet UILabel *          _lblTitle1;
	IBOutlet UILabel *          _lblTitle2;
	IBOutlet UILabel *          _lblTitle3;
	IBOutlet UILabel *          _ctrlIndicator;
	
	IBOutlet UIScrollView *		_MainScrollView;
	
	IBOutlet UIView *           _vwNewsView1;
	IBOutlet UIView *           _vwNewsView2;
	IBOutlet UIView *           _vwNewsView3;
	
	// news table
	IBOutlet UITableView *      _newsTable1;
	IBOutlet UITableView *      _newsTable2;
	IBOutlet UITableView *      _newsTable3;
	
	
	// news badge
	IBOutlet UILabel *      	_lblAncCount;
	IBOutlet UILabel *      	_lblOrderNotify;
	IBOutlet UILabel *      	_lblPersonNotify;
	
	
	/********************** Member Variable ***********************/
	NSMutableArray *            mNewsArray1;
	NSMutableArray *            mNewsArray2;
	NSMutableArray *            mNewsArray3;
	
	int                         mCurPageNum1;
	int                         mCurPageNum2;
	int                         mCurPageNum3;
	
	int                         mCurTab;
}


@property(atomic, readwrite) int announcement_count;
@property(atomic, readwrite) int ordernotify_count;
@property(atomic, readwrite) int personnotify_count;


- (IBAction)onClickedTab1:(id)sender;
- (IBAction)onClickedTab2:(id)sender;
- (IBAction)onClickedTab3:(id)sender;@end
