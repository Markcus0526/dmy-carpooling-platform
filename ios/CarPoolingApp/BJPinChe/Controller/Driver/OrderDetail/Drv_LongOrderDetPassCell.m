//
//  Drv_LongOrderDetPassCell.m
//  BJPinChe
//
//  Created by Kim Ok Chol on 9/5/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "Drv_LongOrderDetPassCell.h"
#import "Drv_LongOrderDetailViewController.h"

@implementation Drv_LongOrderDetPassCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
    }
    return self;
}

- (void)awakeFromNib
{
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}


- (void) initData : (STPassengerInfo *)dataInfo orderInfo:(STOrderInfo *)orderInfo parent:(id)parent
{
	mDataInfo = dataInfo;
	mOrderInfo = orderInfo;
	mParent = parent;

	UITapGestureRecognizer *tap=[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(tapClick)];
    tap.cancelsTouchesInView=NO;
    [headerView addGestureRecognizer:tap];
    
    // set user image
    //_imgUser
    
    // set sex image & change age color according to sex
    
    [_lblAge setText:[NSString stringWithFormat:@"%d", mDataInfo.age]];
    [_lblName setText:mDataInfo.name];
	[_lblSeatCnt setText:mDataInfo.seat_count_desc];
	[_lblCarpoolCnt setText:mDataInfo.carpool_count_desc];

	if(mDataInfo.sex == SEX_MALE) {
		[_imgSex setImage:[UIImage imageNamed:@"icon_male.png"]];
		_lblAge.textColor = [UIColor orangeColor];
	} else {
		[_imgSex setImage:[UIImage imageNamed:@"icon_female.png"]];
		_lblAge.textColor = MYCOLOR_GREEN;
    }


	// check passenger verified state
	if (mDataInfo.pverified == STATE_VERIFIED) {
		[_markVerif setImage:[UIImage imageNamed:@"icon_verified.png"] forState:UIControlStateNormal];
		[_lblDetail setText:@"身份已认证"];
	} else {
		[_markVerif setImage:[UIImage imageNamed:@"icon_unverified.png"] forState:UIControlStateNormal];
		[_lblDetail setText:@"身份未认证"];
	}

	switch (dataInfo.state) {
            //打电话
        case PASS_STATE_UPLOAD:
        case PASS_STATE_NOT_CHECKED:
        case PASS_STATE_WEIJIESUAN:
        {
            [_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_call.png"] forState:UIControlStateNormal];
        }
            break;
            //评价
        case PASS_STATE_PAYED:
        case PASS_STATE_GIVEUP:
        {
            [_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_comment.png"] forState:UIControlStateNormal];
			break;
        }
            //已评价
        case PASS_STATE_WEIPINGJIA:
        {
            switch (mDataInfo.evaluated) {
                case EVAL_NOT:
                    [_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_comment.png"] forState:UIControlStateNormal];
                    break;
                case EVAL_HIGH:
                    [_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_good.png"] forState:UIControlStateNormal];
                    break;
                case EVAL_MEDIUM:
                    [_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_medium.png"] forState:UIControlStateNormal];
                    break;
                case EVAL_LOW:
                    [_btnOperate setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_low.png"] forState:UIControlStateNormal];
                    break;
                default:
                    break;
            }
            break;
        }
            break;
            
            
        default:
            break;
    }
}

- (IBAction)onClickedBtnOperate:(id)sender
{
    Drv_LongOrderDetailViewController * parent = (Drv_LongOrderDetailViewController *)mParent;
    
    [parent onClickedBtnPassOperate:mDataInfo];
}
-(void)tapClick
{
    Drv_LongOrderDetailViewController * parent = (Drv_LongOrderDetailViewController *)mParent;
    
    [parent onClickedBtnPassHeader:mDataInfo];
}
@end
